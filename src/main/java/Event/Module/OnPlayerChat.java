package Event.Module;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("UnusedDeclaration")
public class OnPlayerChat implements IScriptEventHandler {

	// This script is fired on the Module's OnChat event.
	// Note that most of the OnChat scripting takes place within the "reo_mod_chatnwnx" script,
	// as the server uses NWNX_Chat, a plugin for NWNX.

	@Override
	public void runScript(final NWObject objSelf) {
        // DMFI
        NWScript.executeScript("dmfi_onplychat", objSelf);
	}
}