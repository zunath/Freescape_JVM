package Placeable.QuestSystem.ItemCollector;

import Common.IScriptEventHandler;
import GameSystems.QuestSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnOpened implements IScriptEventHandler {
    @Override
    public void runScript(NWObject container) {
        QuestSystem.OnItemCollectorOpened(container);
    }
}
