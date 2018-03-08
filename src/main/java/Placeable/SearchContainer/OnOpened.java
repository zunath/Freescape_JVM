package Placeable.SearchContainer;

import Common.IScriptEventHandler;
import GameSystems.SearchSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("UnusedDeclaration")
public class OnOpened implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        SearchSystem.OnChestOpen(objSelf);
    }
}
