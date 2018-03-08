package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.InventorySystem;
import GameSystems.RadioSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("UnusedDeclaration")
public class OnUnAcquireItem implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {
		RadioSystem radioSystem = new RadioSystem();

		// Bioware Default
		NWScript.executeScript("x2_mod_def_unaqu", objSelf);

		radioSystem.OnModuleUnacquire();
		InventorySystem.OnModuleUnAcquireItem();
	}
}
