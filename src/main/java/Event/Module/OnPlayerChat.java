package Event.Module;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("UnusedDeclaration")
public class OnPlayerChat implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {
        // DMFI
        NWScript.executeScript("dmfi_onplychat", objSelf);
	}
}