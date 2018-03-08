package Item;

import Common.IScriptEventHandler;
import GameSystems.FoodSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class Food implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = NWScript.getItemActivated();
        int amount = NWScript.getLocalInt(oItem, "HUNGER_RESTORE");

        FoodSystem.IncreaseHungerLevel(oPC, amount);
    }
}
