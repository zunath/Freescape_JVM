package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.DeathSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnPlayerDeath implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {

        DeathSystem.OnPlayerDeath();
	}
}

