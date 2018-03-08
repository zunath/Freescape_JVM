package Item;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import Enumerations.QuestID;
import GameSystems.MagicSystem;
import GameSystems.QuestSystem;
import NWNX.NWNX_Events;
import GameSystems.FoodSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class Food implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = NWScript.getItemActivated();
        int amount = NWScript.getLocalInt(oItem, "HUNGER_RESTORE");

        // Snake Eater ability grants +50% to hunger restore.
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.SnakeEater))
        {
            amount *= 1.5f;
        }

        FoodSystem.IncreaseHungerLevel(oPC, amount);

        if(QuestSystem.GetPlayerQuestJournalID(oPC, QuestID.BootCampEating) == 1)
        {
            QuestSystem.AdvanceQuestState(oPC, QuestID.BootCampEating);
        }
    }
}
