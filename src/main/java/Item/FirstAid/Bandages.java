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
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

public class Bandages implements IActionItem {
    @Override
    public Object StartUseItem(NWObject user, NWObject item, NWObject target) {
        NWScript.sendMessageToPC(user, "You begin treating " + NWScript.getName(target, false) + "'s wounds...");
        return null;
    }

    @Override
    public void ApplyEffects(NWObject user, NWObject item, NWObject target, Object customData) {
        ItemGO itemGO = new ItemGO(item);

        CustomEffectSystem.RemovePCCustomEffect(target, CustomEffectType.Bleeding);
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(2), target, 0.0f);
        NWScript.sendMessageToPC(user, "You finish bandaging " + NWScript.getName(target, false) + "'s wounds.");

        PCSkillEntity skill = SkillSystem.GetPCSkill(user, SkillID.FirstAid);
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
        float seconds = 6.0f - (skill.getRank() * 0.2f);
        if(seconds < 1.0f) seconds = 1.0f;
        return seconds;
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
        return true;
    }

    @Override
    public String IsValidTarget(NWObject user, NWObject item, NWObject target) {

        if(!NWScript.getIsPC(target) || NWScript.getIsDM(target))
        {
            return "Only players may be targeted with this item.";
        }

        if(!CustomEffectSystem.DoesPCHaveCustomEffect(target, CustomEffectType.Bleeding))
        {
            return "Your target is not bleeding.";
        }

        return null;
    }
}
