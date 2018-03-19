package Item;

import Common.IScriptEventHandler;
import GameSystems.FoodSystem;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("unused")
public class Food implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = getItemActivated();
        int amount = getLocalInt(oItem, "HUNGER_RESTORE");
        boolean isTainted = getLocalInt(oItem, "HUNGER_TAINTED") == 1;

        FoodSystem.IncreaseHungerLevel(oPC, amount, isTainted);

        int charges = getItemCharges(oItem) - 1;

        if(charges <= 0)
        {
            destroyObject(oItem, 0.0f);
        }
        else
        {
            setItemCharges(oItem, charges);
        }

    }
}
