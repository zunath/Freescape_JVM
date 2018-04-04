package Placeable.PlantSeed;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnOpened implements IScriptEventHandler {
    @Override
    public void runScript(NWObject planter) {
        NWObject oPC = getLastUsedBy();
        setUseableFlag(planter, false);
        sendMessageToPC(oPC, "Place a seed inside to plant it here. Walk away to cancel planting.");
    }
}
