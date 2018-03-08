package Event.Module;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("UnusedDeclaration")
public class OnUnAcquireItem implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {
		// Bioware Default
		NWScript.executeScript("x2_mod_def_unaqu", objSelf);
	}
}
