package Placeable.SearchContainer;

import Common.IScriptEventHandler;
import GameSystems.SearchSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("UnusedDeclaration")
public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        SearchSystem.OnChestDisturbed(objSelf);
    }
}
