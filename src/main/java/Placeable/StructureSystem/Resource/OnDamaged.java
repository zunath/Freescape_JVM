package Placeable.StructureSystem.Resource;

import Common.IScriptEventHandler;
import Data.Repository.SkillRepository;
import Entities.PCSkillEntity;
import Enumerations.SkillID;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import GameSystems.DurabilitySystem;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class OnDamaged implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastDamager(objSelf);
        if(NWScript.getLocalInt(oPC, "NOT_USING_CORRECT_WEAPON") == 1)
        {
            NWScript.deleteLocalInt(oPC, "NOT_USING_CORRECT_WEAPON");
            return;
        }

        PlayerGO pcGO = new PlayerGO(oPC);
        SkillRepository skillRepo = new SkillRepository();
        NWObject oWeapon = NWScript.getLastWeaponUsed(oPC);
        ItemGO weaponGO = new ItemGO(oWeapon);
        NWLocation location = NWScript.getLocation(oPC);
        String resourceItemResref = NWScript.getLocalString(objSelf, "RESOURCE_RESREF");
        int activityID = NWScript.getLocalInt(objSelf, "RESOURCE_ACTIVITY");
        String resourceName = NWScript.getLocalString(objSelf, "RESOURCE_NAME");
        int resourceCount = NWScript.getLocalInt(objSelf, "RESOURCE_COUNT");
        String resourceProp = NWScript.getLocalString(objSelf, "RESOURCE_PROP");
        int difficultyRating = NWScript.getLocalInt(objSelf, "RESOURCE_DIFFICULTY_RATING");
        int baseDurabilityChance = 100;
        int durabilityChanceReduction = 0;
        int durabilityLossChance = baseDurabilityChance - durabilityChanceReduction;
        int weaponChanceBonus;
        int skillID;

        if(activityID == 1) // 1 = Logging
        {
            weaponChanceBonus = weaponGO.getLoggingBonus();
            skillID = SkillID.Logging;
        }
        else if(activityID == 2) // Mining
        {
            weaponChanceBonus = weaponGO.getMiningBonus();
            skillID = SkillID.Mining;
        }
        else return;
        PCSkillEntity skill = skillRepo.GetPCSkillByID(pcGO.getUUID(), skillID);

        if(ThreadLocalRandom.current().nextInt() <= durabilityLossChance)
        {
            DurabilitySystem.RunItemDecay(oPC, oWeapon);
        }

        int baseChance = 10;
        int chance = baseChance + weaponChanceBonus;
        chance = chance + CalculateSuccessChanceDeltaModifier(difficultyRating, skill.getRank());

        if(chance <= 0)
        {
            NWScript.floatingTextStringOnCreature("You do not have enough skill to harvest this resource...", oPC, false);
        }
        else if(ThreadLocalRandom.current().nextInt(100) <= chance)
        {
            NWScript.createObject(ObjectType.ITEM, resourceItemResref, location, false, "");

            NWScript.floatingTextStringOnCreature("You break off some " + resourceName + ".", oPC, false);
            NWScript.setLocalInt(objSelf, "RESOURCE_COUNT", --resourceCount);
            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(10000), objSelf, 0.0f);

            float deltaModifier = CalculateXPDeltaModifier(difficultyRating, skill.getRank());
            float baseXP = (100 + ThreadLocalRandom.current().nextInt(20)) * deltaModifier;
            int xp = (int)SkillSystem.CalculateSkillAdjustedXP(baseXP, weaponGO.getRecommendedLevel(), skill.getRank());
            SkillSystem.GiveSkillXP(oPC, skillID, xp);
        }

        if(resourceCount <= 0)
        {
            if(!Objects.equals(resourceProp, ""))
            {
                NWObject prop = NWScript.getNearestObjectByTag(resourceProp, objSelf, 1);
                NWScript.destroyObject(prop, 0.0f);
            }

            NWScript.destroyObject(objSelf, 0.0f);
        }

    }
    private static float CalculateXPDeltaModifier(int difficultyRating, int skillRank)
    {
        int delta = difficultyRating - skillRank;
        float deltaModifier;

        if(delta >= 9) deltaModifier = 1.9f;
        else if(delta >= 8) deltaModifier = 1.8f;
        else if(delta >= 7) deltaModifier = 1.7f;
        else if(delta >= 6) deltaModifier = 1.6f;
        else if(delta >= 5) deltaModifier = 1.5f;
        else if(delta >= 4) deltaModifier = 1.4f;
        else if(delta >= 3) deltaModifier = 1.3f;
        else if(delta >= 2) deltaModifier = 1.2f;
        else if(delta >= 1) deltaModifier = 1.1f;

        else if(delta <= -10) deltaModifier = 0.0f;
        else if(delta <= -9) deltaModifier = 0.1f;
        else if(delta <= -8) deltaModifier = 0.2f;
        else if(delta <= -7) deltaModifier = 0.3f;
        else if(delta <= -6) deltaModifier = 0.4f;
        else if(delta <= -5) deltaModifier = 0.5f;
        else if(delta <= -4) deltaModifier = 0.6f;
        else if(delta <= -3) deltaModifier = 0.7f;
        else if(delta <= -2) deltaModifier = 0.8f;
        else if(delta <= -1) deltaModifier = 0.9f;
        else deltaModifier = 1.0f;

        return deltaModifier;
    }

    private static int CalculateSuccessChanceDeltaModifier(int difficultyRating, int skillRank)
    {
        int delta = difficultyRating - skillRank;
        int chanceModifier;

        if(delta >= 10) chanceModifier = -999;
        else if(delta >= 9) chanceModifier = -100;
        else if(delta >= 8) chanceModifier = -90;
        else if(delta >= 7) chanceModifier = -80;
        else if(delta >= 6) chanceModifier = -70;
        else if(delta >= 5) chanceModifier = -60;
        else if(delta >= 4) chanceModifier = -40;
        else if(delta >= 3) chanceModifier = -30;
        else if(delta >= 2) chanceModifier = -10;
        else if(delta >= 1) chanceModifier = -5;

        else if(delta <= -10) chanceModifier = 90;
        else if(delta <= -9) chanceModifier = 80;
        else if(delta <= -8) chanceModifier = 75;
        else if(delta <= -7) chanceModifier = 70;
        else if(delta <= -6) chanceModifier = 60;
        else if(delta <= -5) chanceModifier = 40;
        else if(delta <= -4) chanceModifier = 30;
        else if(delta <= -3) chanceModifier = 20;
        else if(delta <= -2) chanceModifier = 10;
        else if(delta <= -1) chanceModifier = 5;
        else chanceModifier = 0;

        return chanceModifier;
    }
}
