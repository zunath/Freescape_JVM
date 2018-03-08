package GameSystems;

import CustomEffect.ICustomEffectHandler;
import Data.Repository.CustomEffectRepository;
import Entities.CustomEffectEntity;
import Entities.PCCustomEffectEntity;
import GameObject.PlayerGO;
import GameSystems.Models.CasterSpellModel;
import Helper.ErrorHelper;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.EffectType;

import java.util.*;

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
        String areaResref = NWScript.getResRef(NWScript.getArea(oPC));

        for(PCCustomEffectEntity effect : effects)
        {
            if(NWScript.getCurrentHitPoints(oPC) <= -11 || areaResref.equals("death_realm"))
            {
                RemovePCCustomEffect(oPC, effect.getCustomEffect().getCustomEffectID());
                return;
            }

            PCCustomEffectEntity result = RunPCCustomEffectProcess(oPC, effect);
            if(result == null)
            {
                NWScript.sendMessageToPC(oPC, effect.getCustomEffect().getWornOffMessage());
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

            try {
                Class scriptClass = Class.forName("CustomEffect." + entity.getScriptHandler());
                ICustomEffectHandler script = (ICustomEffectHandler)scriptClass.newInstance();
                script.run(casterModel.getCaster(), casterModel.getTarget());
                Scheduler.flushQueues();
            }
            catch(Exception ex)
            {
                ErrorHelper.HandleException(ex, "OnModuleHeartbeat was unable to run specific effect script: " + entity.getScriptHandler());
            }


            // Kill the effect if it has expired, target is invalid, or target is dead.
            if(entry.getValue() <= 0 ||
                    !NWScript.getIsObjectValid(casterModel.getTarget()) ||
                    NWScript.getCurrentHitPoints(casterModel.getTarget()) <= -11 ||
                    NWScript.getIsDead(casterModel.getTarget()))
            {
                effectsToRemove.add(entry.getKey());

                if(NWScript.getIsObjectValid(casterModel.getCaster()) && NWScript.getIsPC(casterModel.getCaster()))
                {
                    NWScript.sendMessageToPC(casterModel.getCaster(), "Your effect '" + casterModel.getEffectName() + "' has worn off of " + NWScript.getName(casterModel.getTarget(), false));
                }
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

        NWScript.sendMessageToPC(oPC, effect.getCustomEffect().getContinueMessage());
        try {
            Class scriptClass = Class.forName("CustomEffect." + effect.getCustomEffect().getScriptHandler());
            ICustomEffectHandler script = (ICustomEffectHandler)scriptClass.newInstance();
            script.run(null, oPC);
            Scheduler.flushQueues();
        }
        catch(Exception ex) {
            ErrorHelper.HandleException(ex, "RunPCCustomEffectProcess was unable to run specific effect script: " + effect.getCustomEffect().getScriptHandler());
        }

        return effect;
    }

    public static void ApplyCustomEffect(NWObject oCaster, NWObject oTarget, int customEffectID, int ticks)
    {
        // No custom effects can be applied if player is under the effect of sanctuary.
        for(NWEffect effect : NWScript.getEffects(oTarget))
        {
            int type = NWScript.getEffectType(effect);
            if(type == EffectType.SANCTUARY) return;
        }


        CustomEffectRepository repo = new CustomEffectRepository();
        CustomEffectEntity effectEntity = repo.GetEffectByID(customEffectID);

        // PC custom effects are tracked in the database.
        if(NWScript.getIsPC(oTarget) && !NWScript.getIsDM(oTarget) && !NWScript.getIsDMPossessed(oTarget))
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

            NWScript.sendMessageToPC(oTarget, effectEntity.getStartMessage());
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

        // Serious limitations in NWN with adding custom effects / using custom icons. Commented out for now.
        //EffectHelper.ApplyEffectIcon(oPC, effectEntity.getIconID(), ticks * 6.0f);
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

        if(effect == null) return;

        repo.Delete(effect);
        NWScript.sendMessageToPC(oPC, effect.getCustomEffect().getWornOffMessage());
    }
}
