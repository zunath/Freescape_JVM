package Item.FirstAid;

import Data.Repository.PlayerRepository;
import Entities.PCSkillEntity;
import Entities.PlayerEntity;
import Enumerations.BackgroundID;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Item.IActionItem;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.EffectType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class HealingKit implements IActionItem {

    @Override
    public Object StartUseItem(NWObject user, NWObject item, NWObject target) {
        sendMessageToPC(user, "You begin treating " + getName(target, false) + "'s wounds...");
        return null;
    }

    @Override
    public void ApplyEffects(NWObject user, NWObject item, NWObject target, Object customData) {

        PlayerGO targetGO = new PlayerGO(target);
        ItemGO itemGO = new ItemGO(item);

        targetGO.removeEffect(EffectType.REGENERATE);
        PCSkillEntity skill = SkillSystem.GetPCSkill(user, SkillID.FirstAid);
        int luck = PerkSystem.GetPCPerkLevel(user, PerkID.Lucky);
        int perkDurationBonus = PerkSystem.GetPCPerkLevel(user, PerkID.HealingKitExpert) * 6 + (luck * 2);
        float duration = 30.0f + (skill.getRank() * 0.4f) + perkDurationBonus;
        final int restoreAmount = 1 + getLocalInt(item, "HEALING_BONUS");

        int perkBlastBonus = PerkSystem.GetPCPerkLevel(user, PerkID.ImmediateImprovement);
        if(perkBlastBonus > 0)
        {
            int blastHeal = restoreAmount * perkBlastBonus;
            if(ThreadLocalRandom.current().nextInt(100) + 1 <= luck / 2)
            {
                blastHeal *= 2;
            }
            applyEffectToObject(DurationType.INSTANT, effectHeal(blastHeal), target, 0.0f);
        }

        NWEffect regeneration = effectRegenerate(restoreAmount, 6.0f);
        applyEffectToObject(DurationType.TEMPORARY, regeneration, target, duration);
        sendMessageToPC(user, "You successfully treat " + getName(target, false) + "'s wounds.");

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
    public boolean ReducesItemCharge(NWObject user, NWObject item, NWObject target, Object customdata) {
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(user);
        PlayerEntity pcEntity = playerRepo.GetByPlayerID(pcGO.getUUID());
        int consumeChance = PerkSystem.GetPCPerkLevel(user, PerkID.FrugalMedic) * 10;

        if(pcEntity.getBackgroundID() == BackgroundID.Medic)
        {
            consumeChance += 5;
        }


        return ThreadLocalRandom.current().nextInt(100) + 1 > consumeChance;
    }

    @Override
    public String IsValidTarget(NWObject user, NWObject item, NWObject target) {

        if(!getIsPC(target) || getIsDM(target))
        {
            return "Only players may be targeted with this item.";
        }

        if(getCurrentHitPoints(target) >= getMaxHitPoints(target))
        {
            return "Your target is not hurt.";
        }

        return null;
    }
}
