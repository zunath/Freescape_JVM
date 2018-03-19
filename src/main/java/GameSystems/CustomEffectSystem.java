package GameSystems;

import CustomEffect.ICustomEffectHandler;
import Data.Repository.CustomEffectRepository;
import Entities.CustomEffectEntity;
import Entities.PCCustomEffectEntity;
import GameObject.PlayerGO;
import GameSystems.Models.CasterSpellModel;
import Helper.ErrorHelper;
import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.EffectType;

import java.util.*;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class CustomEffectSystem {
    private static HashMap<CasterSpellModel, Integer> npcEffectList;
    private static ArrayList<CasterSpellModel> effectsToRemove;

    static
    {
        npcEffectList = new HashMap<>();
        effectsToRemove = new ArrayList<>();
    }

    public static void OnPlayerHeartbeat(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        CustomEffectRepository repo = new CustomEffectRepository();
        List<PCCustomEffectEntity> effects = repo.GetPCEffects(pcGO.getUUID());
        String areaResref = getResRef(getArea(oPC));

        for(PCCustomEffectEntity effect : effects)
        {
            if(getCurrentHitPoints(oPC) <= -11 || areaResref.equals("death_realm"))
            {
                RemovePCCustomEffect(oPC, effect.getCustomEffect().getCustomEffectID());
                return;
            }

            PCCustomEffectEntity result = RunPCCustomEffectProcess(oPC, effect);
            if(result == null)
            {
                sendMessageToPC(oPC, effect.getCustomEffect().getWornOffMessage());
                ICustomEffectHandler handler = (ICustomEffectHandler)ScriptHelper.GetClassByName("CustomEffect." + effect.getCustomEffect().getScriptHandler());
                if(handler != null)
                {
                    handler.WearOff(null, oPC);
                }

                deleteLocalInt(oPC, "CUSTOM_EFFECT_ACTIVE_" + effect.getCustomEffect().getCustomEffectID());
                repo.Delete(effect);
            }
            else
            {
                repo.Save(effect);
            }
        }
    }

    public static void OnModuleHeartbeat()
    {
        CustomEffectRepository repo = new CustomEffectRepository();

        for(Map.Entry<CasterSpellModel, Integer> entry : npcEffectList.entrySet())
        {
            CasterSpellModel casterModel = entry.getKey();
            entry.setValue(entry.getValue() - 1);
            CustomEffectEntity entity = repo.GetEffectByID(casterModel.getCustomEffectID());
            ICustomEffectHandler handler = (ICustomEffectHandler) ScriptHelper.GetClassByName("CustomEffect." + entity.getScriptHandler());

            try {
                if(handler != null)
                {
                    handler.Tick(casterModel.getCaster(), casterModel.getTarget());
                }
            }
            catch(Exception ex)
            {
                ErrorHelper.HandleException(ex, "OnModuleHeartbeat was unable to run specific effect script: " + entity.getScriptHandler());
            }


            // Kill the effect if it has expired, target is invalid, or target is dead.
            if(entry.getValue() <= 0 ||
                    !getIsObjectValid(casterModel.getTarget()) ||
                    getCurrentHitPoints(casterModel.getTarget()) <= -11 ||
                    getIsDead(casterModel.getTarget()))
            {
                effectsToRemove.add(entry.getKey());

                if(handler != null)
                {
                    handler.WearOff(casterModel.getCaster(), casterModel.getTarget());
                }

                if(getIsObjectValid(casterModel.getCaster()) && getIsPC(casterModel.getCaster()))
                {
                    sendMessageToPC(casterModel.getCaster(), "Your effect '" + casterModel.getEffectName() + "' has worn off of " + getName(casterModel.getTarget(), false));
                }

                deleteLocalInt(casterModel.getTarget(), "CUSTOM_EFFECT_ACTIVE_" + casterModel.getCustomEffectID());
            }
        }

        for(CasterSpellModel entry : effectsToRemove)
        {
            npcEffectList.remove(entry);
        }
        effectsToRemove.clear();
    }

    private static PCCustomEffectEntity RunPCCustomEffectProcess(NWObject oPC, PCCustomEffectEntity effect)
    {
        effect.setTicks(effect.getTicks() - 1);
        if(effect.getTicks() < 0) return null;

        if(!effect.getCustomEffect().getContinueMessage().equals(""))
        {
            sendMessageToPC(oPC, effect.getCustomEffect().getContinueMessage());
        }

        ICustomEffectHandler handler = (ICustomEffectHandler)ScriptHelper.GetClassByName("CustomEffect." + effect.getCustomEffect().getScriptHandler());
        if(handler != null)
        {
            handler.Tick(null, oPC);
        }

        return effect;
    }

    public static void ApplyCustomEffect(NWObject oCaster, NWObject oTarget, int customEffectID, int ticks, int effectLevel)
    {
        // Can't apply the effect if the existing one is stronger.
        int existingEffectLevel = GetActiveEffectLevel(oTarget, customEffectID);
        if(existingEffectLevel > effectLevel)
        {
            sendMessageToPC(oCaster, "A more powerful effect already exists on your target.");
            return;
        }

        // No custom effects can be applied if player is under the effect of sanctuary.
        for(NWEffect effect : getEffects(oTarget))
        {
            int type = getEffectType(effect);
            if(type == EffectType.SANCTUARY) return;
        }

        CustomEffectRepository repo = new CustomEffectRepository();
        CustomEffectEntity effectEntity = repo.GetEffectByID(customEffectID);

        // PC custom effects are tracked in the database.
        if(getIsPC(oTarget) && !getIsDM(oTarget) && !getIsDMPossessed(oTarget))
        {
            PlayerGO pcGO = new PlayerGO(oTarget);
            PCCustomEffectEntity entity = repo.GetPCEffectByID(pcGO.getUUID(), customEffectID);

            if(entity == null)
            {
                entity = new PCCustomEffectEntity();
                entity.setPlayerID(pcGO.getUUID());
                entity.setCustomEffect(effectEntity);
            }

            entity.setTicks(ticks);
            repo.Save(entity);

            sendMessageToPC(oTarget, effectEntity.getStartMessage());
        }
        // NPCs custom effects are tracked in server memory.
        else
        {
            // Look for existing effect.
            for(Map.Entry<CasterSpellModel, Integer> entry : npcEffectList.entrySet())
            {
                CasterSpellModel casterSpellModel = entry.getKey();

                if(Objects.equals(casterSpellModel.getCaster(), oCaster) &&
                   casterSpellModel.getCustomEffectID() == customEffectID &&
                   Objects.equals(casterSpellModel.getTarget(), oTarget))
                {
                    entry.setValue(ticks);
                    return;
                }
            }

            // Didn't find an existing effect. Create a new one.
            CasterSpellModel spellModel = new CasterSpellModel();
            spellModel.setCaster(oCaster);
            spellModel.setCustomEffectID(customEffectID);
            spellModel.setEffectName(effectEntity.getName());
            spellModel.setTarget(oTarget);

            npcEffectList.put(spellModel, ticks);
        }

        setLocalInt(oTarget, "CUSTOM_EFFECT_ACTIVE_" + customEffectID, effectLevel);
    }

    public static boolean DoesPCHaveCustomEffect(NWObject oPC, int customEffectID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        CustomEffectRepository repo = new CustomEffectRepository();
        PCCustomEffectEntity entity = repo.GetPCEffectByID(pcGO.getUUID(), customEffectID);

        return entity != null;
    }

    public static void RemovePCCustomEffect(NWObject oPC, int customEffectID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        CustomEffectRepository repo = new CustomEffectRepository();
        PCCustomEffectEntity effect = repo.GetPCEffectByID(pcGO.getUUID(), customEffectID);
        NWScript.deleteLocalInt(oPC, "CUSTOM_EFFECT_ACTIVE_" + customEffectID);

        if(effect == null) return;

        repo.Delete(effect);
        sendMessageToPC(oPC, effect.getCustomEffect().getWornOffMessage());
    }

    public static int GetActiveEffectLevel(NWObject oTarget, int customEffectID)
    {
        String varName = "CUSTOM_EFFECT_ACTIVE_" + customEffectID;
        return getLocalInt(oTarget, varName);
    }

}
