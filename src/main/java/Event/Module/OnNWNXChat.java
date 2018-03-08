package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.ActivityLoggingSystem;
import GameSystems.RadioSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnNWNXChat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        RadioSystem radioSystem = new RadioSystem();

        radioSystem.OnNWNXChat(objSelf);

        ActivityLoggingSystem.OnModuleNWNXChat(objSelf);
    }
}
