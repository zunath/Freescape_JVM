package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.ActivityLoggingSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnNWNXChat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        ActivityLoggingSystem.OnModuleNWNXChat(objSelf);
    }
}
