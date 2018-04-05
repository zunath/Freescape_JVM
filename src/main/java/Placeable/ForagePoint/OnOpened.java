package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import Entities.PCSkillEntity;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameSystems.FoodSystem;
import GameSystems.LootSystem;
import GameSystems.Models.ItemModel;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.AnimationLooping;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnOpened implements IScriptEventHandler {
    @Override
    public void runScript(NWObject point) {

        final int chanceToFullyHarvest = 50;
        boolean alwaysDestroys = getLocalInt(point, "FORAGE_POINT_ALWAYS_DESTROYS") == 1;


        NWObject oPC = getLastOpenedBy();
        boolean hasBeenSearched = getLocalInt(point, "FORAGE_POINT_FULLY_HARVESTED") == 1;
        if(hasBeenSearched)
        {
            sendMessageToPC(oPC, "There's nothing left to harvest here...");
            return;
        }

        // Not fully harvested but the timer hasn't counted down yet.
        int refillTick = getLocalInt(point, "FORAGE_POINT_REFILL_TICKS");
        if(refillTick > 0)
        {
            sendMessageToPC(oPC, "You couldn't find anything new here. Check back later...");
            return;
        }

        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;
        PCSkillEntity pcSkill = SkillSystem.GetPCSkill(oPC, SkillID.Forage);
        if(pcSkill == null) return;

        int lootTableID = getLocalInt(point, "FORAGE_POINT_LOOT_TABLE_ID");
        int level = getLocalInt(point, "FORAGE_POINT_LEVEL");
        int rank = pcSkill.getRank();
        int delta = level - rank;

        if(delta > 8)
        {
            sendMessageToPC(oPC, "You aren't skilled enough to forage through this.");
            Scheduler.assign(oPC, () -> actionInteractObject(point));
            return;
        }

        int dc = 6 + delta;
        if(dc <= 4) dc = 4;
        int searchAttempts = 1 + CalculateSearchAttempts(oPC);

        int luck = PerkSystem.GetPCPerkLevel(oPC, PerkID.Lucky);
        if(ThreadLocalRandom.current().nextInt(100) + 1 <= luck / 2)
        {
            dc--;
        }

        Scheduler.assign(oPC, () -> actionPlayAnimation(AnimationLooping.GET_LOW, 1.0f, 2.0f));

        for(int attempt = 1; attempt <= searchAttempts; attempt++)
        {
            int roll = ThreadLocalRandom.current().nextInt(20) + 1;
            if(roll >= dc)
            {
                NWScript.floatingTextStringOnCreature(ColorToken.SkillCheck() + "Search: *success*: (" + roll + " vs. DC: " + dc + ")" + ColorToken.End(), oPC, false);
                ItemModel spawnItem = LootSystem.PickRandomItemFromLootTable(lootTableID);

                if(spawnItem == null)
                {
                    return;
                }

                if(!spawnItem.getResref().equals("") && spawnItem.getQuantity() > 0)
                {
                    NWScript.createItemOnObject(spawnItem.getResref(), point, spawnItem.getQuantity(), "");
                }

                float xp = SkillSystem.CalculateSkillAdjustedXP(50, level, rank);
                SkillSystem.GiveSkillXP(oPC, SkillID.Forage, (int)xp);
            }
            else
            {
                NWScript.floatingTextStringOnCreature(ColorToken.SkillCheck() + "Search: *failure*: (" + roll + " vs. DC: " + dc + ")" + ColorToken.End(), oPC, false);

                float xp = SkillSystem.CalculateSkillAdjustedXP(10, level, rank);
                SkillSystem.GiveSkillXP(oPC, SkillID.Forage, (int)xp);
            }
            dc += ThreadLocalRandom.current().nextInt(3) + 1;
        }

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= 3)
        {
            FoodSystem.DecreaseHungerLevel(oPC, 1);
        }

        // Chance to destroy the forage point.
        if(alwaysDestroys || ThreadLocalRandom.current().nextInt(100) + 1 <= chanceToFullyHarvest)
        {
            setLocalInt(point, "FORAGE_POINT_FULLY_HARVESTED", 1);
            sendMessageToPC(oPC, "This resource has been fully harvested...");
        }
        // Otherwise the forage point will be refilled in 10-20 minutes.
        else
        {
            setLocalInt(point, "FORAGE_POINT_REFILL_TICKS", 100 + ThreadLocalRandom.current().nextInt(100));
        }

        setLocalInt(point, "FORAGE_POINT_DESPAWN_TICKS", 30);
    }


    private int CalculateSearchAttempts(NWObject oPC)
    {
        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.ForageExpert);

        int numberOfSearches = 0;
        int attempt1Chance = 0;
        int attempt2Chance = 0;

        switch (perkLevel)
        {
            case 1: attempt1Chance = 10; break;
            case 2: attempt1Chance = 20; break;
            case 3: attempt1Chance = 30; break;
            case 4: attempt1Chance = 40; break;
            case 5: attempt1Chance = 50; break;

            case 6:
                attempt1Chance = 50;
                attempt2Chance = 10;
                break;
            case 7:
                attempt1Chance = 50;
                attempt2Chance = 20;
                break;
            case 8:
                attempt1Chance = 50;
                attempt2Chance = 30;
                break;
            case 9:
                attempt1Chance = 50;
                attempt2Chance = 40;
                break;
            case 10:
                attempt1Chance = 50;
                attempt2Chance = 50;
                break;
        }

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= attempt1Chance)
        {
            numberOfSearches++;
        }
        if(ThreadLocalRandom.current().nextInt(100) + 1 <= attempt2Chance)
        {
            numberOfSearches++;
        }

        return numberOfSearches;
    }

}
