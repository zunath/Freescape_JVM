package Placeable.StorageContainer;

import Common.IScriptEventHandler;
import GameSystems.StorageSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("unused")
public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oChest) {
        StorageSystem.OnChestDisturbed(oChest);
    }
}
