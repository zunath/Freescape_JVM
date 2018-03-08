package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.DeathSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("UnusedDeclaration")
public class OnPlayerRespawn implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
        DeathSystem.OnPlayerRespawn();
	}
}
