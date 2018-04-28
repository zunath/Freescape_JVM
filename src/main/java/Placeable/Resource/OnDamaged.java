package Placeable.Resource;

import Common.IScriptEventHandler;
import Data.Repository.SkillRepository;
import Entities.PCSkillEntity;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import GameSystems.DurabilitySystem;
import GameSystems.FoodSystem;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnDamaged implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = getLastDamager(objSelf);
        if(getLocalInt(oPC, "NOT_USING_CORRECT_WEAPON") == 1)
        {
            deleteLocalInt(oPC, "NOT_USING_CORRECT_WEAPON");
            return;
        }

        PlayerGO pcGO = new PlayerGO(oPC);
        SkillRepository skillRepo = new SkillRepository();
        NWObject oWeapon = getLastWeaponUsed(oPC);
        ItemGO weaponGO = new ItemGO(oWeapon);
        NWLocation location = getLocation(oPC);
        String resourceItemResref = getLocalString(objSelf, "RESOURCE_RESREF");
        int activityID = getLocalInt(objSelf, "RESOURCE_ACTIVITY");
        String resourceName = getLocalString(objSelf, "RESOURCE_NAME");
        int resourceCount = getLocalInt(objSelf, "RESOURCE_COUNT");
        int difficultyRating = getLocalInt(objSelf, "RESOURCE_DIFFICULTY_RATING");
        int weaponChanceBonus;
        int skillID;
        int perkChanceBonus;
        int secondResourceChance;
        int durabilityChanceReduction = 0;
        int hasteChance;
        int lucky = PerkSystem.GetPCPerkLevel(oPC, PerkID.Lucky);
        boolean hasBaggerPerk;

        if(activityID == 1) // 1 = Logging
        {
            weaponChanceBonus = weaponGO.getLoggingBonus();
            if(weaponChanceBonus > 0)
            {
                weaponChanceBonus += PerkSystem.GetPCPerkLevel(oPC, PerkID.LoggingAxeExpert) * 5;
                durabilityChanceReduction = PerkSystem.GetPCPerkLevel(oPC, PerkID.LoggingAxeExpert) * 10 + lucky;
            }

            skillID = SkillID.Logging;
            perkChanceBonus = PerkSystem.GetPCPerkLevel(oPC, PerkID.Lumberjack) * 5 + lucky;
            secondResourceChance = PerkSystem.GetPCPerkLevel(oPC, PerkID.PrecisionLogging) * 10;
            hasteChance = PerkSystem.GetPCPerkLevel(oPC, PerkID.SpeedyLogger) * 10 + lucky;
            hasBaggerPerk = PerkSystem.GetPCPerkLevel(oPC, PerkID.WoodBagger) > 0;
        }
        else if(activityID == 2) // Mining
        {
            weaponChanceBonus = weaponGO.getMiningBonus();
            if(weaponChanceBonus > 0)
            {
                weaponChanceBonus += PerkSystem.GetPCPerkLevel(oPC, PerkID.PickaxeExpert) * 5;
                durabilityChanceReduction = PerkSystem.GetPCPerkLevel(oPC, PerkID.PickaxeExpert) * 10 + lucky;
            }
            skillID = SkillID.Mining;
            perkChanceBonus = PerkSystem.GetPCPerkLevel(oPC, PerkID.Miner) * 5 + lucky;
            secondResourceChance = PerkSystem.GetPCPerkLevel(oPC, PerkID.PrecisionMining) * 10;
            hasteChance = PerkSystem.GetPCPerkLevel(oPC, PerkID.SpeedyMiner) * 10 + lucky;
            hasBaggerPerk = PerkSystem.GetPCPerkLevel(oPC, PerkID.OreBagger) > 0;
        }
        else return;
        PCSkillEntity skill = skillRepo.GetPCSkillByID(pcGO.getUUID(), skillID);
        int durabilityLossChance = 100 - durabilityChanceReduction;
        if(ThreadLocalRandom.current().nextInt() <= durabilityLossChance)
        {
            DurabilitySystem.RunItemDecay(oPC, oWeapon);
        }

        int baseChance = 10;
        int chance = baseChance + weaponChanceBonus;
        chance += CalculateSuccessChanceDeltaModifier(difficultyRating, skill.getRank());
        chance += perkChanceBonus;

        boolean givePityItem = false;
        if(chance > 0)
        {
            if(ThreadLocalRandom.current().nextInt(100) + 1 <= hasteChance)
            {
                applyEffectToObject(DurationType.TEMPORARY, effectHaste(), oPC, 8.0f);
            }

            // Give an item if the player hasn't gotten anything after 6-8 attempts.
            int attemptFailureCount = getLocalInt(oPC, "RESOURCE_ATTEMPT_FAILURE_COUNT") + 1;
            NWObject resource = getLocalObject(oPC, "RESOURCE_ATTEMPT_FAILURE_OBJECT");

            if(!getIsObjectValid(resource) || !resource.equals(objSelf))
            {
                resource = objSelf;
                attemptFailureCount = 1;
            }

            int pityItemChance = 0;
            if(attemptFailureCount == 6) pityItemChance = 60;
            else if(attemptFailureCount == 7) pityItemChance = 80;
            else if(attemptFailureCount >= 8) pityItemChance = 100;

            if(ThreadLocalRandom.current().nextInt(100) + 1 <= pityItemChance)
            {
                givePityItem = true;
                attemptFailureCount = 0;
            }

            setLocalInt(oPC, "RESOURCE_ATTEMPT_FAILURE_COUNT", attemptFailureCount);
            setLocalObject(oPC, "RESOURCE_ATTEMPT_FAILURE_OBJECT", resource);
        }

        if(chance <= 0)
        {
            floatingTextStringOnCreature("You do not have enough skill to harvest this resource...", oPC, false);
        }
        else if(ThreadLocalRandom.current().nextInt(100) <= chance || givePityItem)
        {
            if(hasBaggerPerk)
            {
                createItemOnObject(resourceItemResref, oPC, 1, "");
            }
            else
            {
                createObject(ObjectType.ITEM, resourceItemResref, location, false, "");
            }


            floatingTextStringOnCreature("You break off some " + resourceName + ".", oPC, false);
            setLocalInt(objSelf, "RESOURCE_COUNT", --resourceCount);
            applyEffectToObject(DurationType.INSTANT, effectHeal(10000), objSelf, 0.0f);

            if(ThreadLocalRandom.current().nextInt(100) + 1 <= secondResourceChance)
            {
                floatingTextStringOnCreature("You break off a second piece.", oPC, false);

                if(hasBaggerPerk)
                {
                    createItemOnObject(resourceItemResref, oPC, 1, "");
                }
                else
                {
                    createObject(ObjectType.ITEM, resourceItemResref, location, false, "");
                }
            }

            float deltaModifier = CalculateXPDeltaModifier(difficultyRating, skill.getRank());
            float baseXP = (100 + ThreadLocalRandom.current().nextInt(20)) * deltaModifier;
            int xp = (int)SkillSystem.CalculateSkillAdjustedXP(baseXP, weaponGO.getRecommendedLevel(), skill.getRank());
            SkillSystem.GiveSkillXP(oPC, skillID, xp);

            deleteLocalInt(oPC, "RESOURCE_ATTEMPT_FAILURE_COUNT");
            deleteLocalInt(oPC, "RESOURCE_ATTEMPT_FAILURE_OBJECT");
        }

        if(resourceCount <= 0)
        {
            SpawnSeed(objSelf, oPC);

            NWObject prop = getLocalObject(objSelf, "RESOURCE_PROP_OBJ");
            if(getIsObjectValid(prop))
            {
                destroyObject(prop, 0.0f);
            }

            destroyObject(objSelf, 0.0f);
        }
    }

    private static void SpawnSeed(NWObject objSelf, NWObject oPC)
    {
        NWLocation location = getLocation(objSelf);
        String resourceSeedResref = getLocalString(objSelf, "RESOURCE_SEED_RESREF");

        if(!resourceSeedResref.equals(""))
        {
            createObject(ObjectType.ITEM, resourceSeedResref, location, false, "");

            int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.SeedSearcher);
            if(perkLevel <= 0) return;

            if(ThreadLocalRandom.current().nextInt(100) + 1 <= perkLevel * 10)
            {
                createObject(ObjectType.ITEM, resourceSeedResref, location, false, "");
            }
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
