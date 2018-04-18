package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.ActivityLoggingSystem;
import GameSystems.ChatCommandSystem;
import GameSystems.PlayerDescriptionSystem;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnNWNXChat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject sender) {
        ActivityLoggingSystem.OnModuleNWNXChat(sender);
        ChatCommandSystem.OnModuleNWNXChat(sender);
        PlayerDescriptionSystem.OnModuleNWNXChat(sender);
        StructureSystem.OnModuleNWNXChat(sender);
    }
}
