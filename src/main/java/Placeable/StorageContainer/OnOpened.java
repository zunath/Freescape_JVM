package Placeable.StorageContainer;

import Common.IScriptEventHandler;
import GameSystems.StorageSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("unused")
public class OnOpened implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oChest) {
        StorageSystem.OnChestOpened(oChest);
    }
}
