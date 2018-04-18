package Item.FirstAid;

import Entities.PCSkillEntity;
import Enumerations.CustomEffectType;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameObject.ItemGO;
import GameSystems.CustomEffectSystem;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Item.IActionItem;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.EffectType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class TreatmentKit implements IActionItem {
    @Override
    public Object StartUseItem(NWObject user, NWObject item, NWObject target) {
        sendMessageToPC(user, "You begin treating " + getName(target, false) + "'s infection...");
        return null;
    }

    @Override
    public void ApplyEffects(NWObject user, NWObject item, NWObject target, Object customData) {

        CustomEffectSystem.RemovePCCustomEffect(target, CustomEffectType.Poison);

        for(NWEffect effect: getEffects(target))
        {
            if(getIsEffectValid(effect))
            {
                int effectType = getEffectType(effect);
                if(effectType == EffectType.POISON || effectType == EffectType.DISEASE)
                {
                    removeEffect(target, effect);
                }
            }
        }

        sendMessageToPC(user, "You successfully treat " + getName(target, false) + "'s infection.");

        PCSkillEntity skill = SkillSystem.GetPCSkill(user, SkillID.FirstAid);
        ItemGO itemGO = new ItemGO(item);
        int xp = (int)SkillSystem.CalculateSkillAdjustedXP(100, itemGO.getRecommendedLevel(), skill.getRank());
        SkillSystem.GiveSkillXP(user, SkillID.FirstAid, xp);
    }

    @Override
    public float Seconds(NWObject user, NWObject item, NWObject target, Object customData) {

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= PerkSystem.GetPCPerkLevel(user, PerkID.SpeedyMedic) * 10)
        {
            return 0.1f;
        }

        PCSkillEntity skill = SkillSystem.GetPCSkill(user, SkillID.FirstAid);
        return 12.0f - (skill.getRank() * 0.1f);
    }

    @Override
    public boolean FaceTarget() {
        return true;
    }

    @Override
    public int AnimationID() {
        return Animation.LOOPING_GET_MID;
    }

    @Override
    public float MaxDistance() {
        return 3.5f;
    }

    @Override
    public boolean ReducesItemCharge(NWObject user, NWObject item, NWObject target, Object customData) {
        int consumeChance = PerkSystem.GetPCPerkLevel(user, PerkID.FrugalMedic) * 10;
        return ThreadLocalRandom.current().nextInt(100) + 1 > consumeChance;
    }

    @Override
    public String IsValidTarget(NWObject user, NWObject item, NWObject target) {

        if(!NWScript.getIsPC(target) || NWScript.getIsDM(target))
        {
            return "Only players may be targeted with this item.";
        }

        boolean hasEffect = false;
        NWEffect[] effects = getEffects(target);
        for(NWEffect effect: effects)
        {
            if(getIsEffectValid(effect))
            {
                int effectType = getEffectType(effect);
                if(effectType == EffectType.POISON || effectType == EffectType.DISEASE)
                {
                    hasEffect = true;
                }
            }
        }

        if(CustomEffectSystem.DoesPCHaveCustomEffect(target, CustomEffectType.Poison))
        {
            hasEffect = true;
        }

        if(!hasEffect)
        {
            return "This player is not diseased or poisoned.";
        }

        return null;
    }
}
