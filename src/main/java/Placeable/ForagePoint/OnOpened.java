package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import Entities.PCSkillEntity;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameSystems.LootSystem;
import GameSystems.Models.ItemModel;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnOpened implements IScriptEventHandler {
    @Override
    public void runScript(NWObject point) {

        NWObject oPC = getLastOpenedBy();
        boolean hasBeenSearched = getLocalInt(point, "FORAGE_POINT_HAS_BEEN_SEARCHED") == 1;
        if(hasBeenSearched)
        {
            sendMessageToPC(oPC, "Someone has already searched this...");
            return;
        }


        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;
        PCSkillEntity pcSkill = SkillSystem.GetPCSkill(oPC, SkillID.Survival);
        if(pcSkill == null) return;

        int lootTableID = getLocalInt(point, "FORAGE_POINT_LOOT_TABLE_ID");
        int level = getLocalInt(point, "FORAGE_POINT_LEVEL");
        int rank = pcSkill.getRank();
        int delta = level - rank;

        if(delta > 8)
        {
            sendMessageToPC(oPC, "You aren't skilled enough to forage through this.");
            return;
        }

        int dc = 8 + delta;
        if(dc <= 4) dc = 4;
        int searchAttempts = 1; // todo: perk which increases searches

        int luck = PerkSystem.GetPCPerkLevel(oPC, PerkID.Lucky);
        if(ThreadLocalRandom.current().nextInt(100) + 1 <= luck / 2)
        {
            dc--;
        }

        for(int attempt = 1; attempt <= searchAttempts; attempt++)
        {
            int roll = ThreadLocalRandom.current().nextInt(20);
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
                SkillSystem.GiveSkillXP(oPC, SkillID.Survival, (int)xp);
            }
            else
            {
                NWScript.floatingTextStringOnCreature(ColorToken.SkillCheck() + "Search: *failure*: (" + roll + " vs. DC: " + dc + ")" + ColorToken.End(), oPC, false);

                float xp = SkillSystem.CalculateSkillAdjustedXP(10, level, rank);
                SkillSystem.GiveSkillXP(oPC, SkillID.Survival, (int)xp);
            }
            dc += ThreadLocalRandom.current().nextInt(3) + 1;
        }

        setLocalInt(point, "FORAGE_POINT_HAS_BEEN_SEARCHED", 1);
        setLocalInt(point, "FORAGE_POINT_DESPAWN_TICKS", 30);
    }
}
