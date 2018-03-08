package Event.Module;
import Dialog.DialogManager;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.RestEventtype;

@SuppressWarnings("unused")
public class OnPlayerRest implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {
        final NWObject oPC = NWScript.getLastPCRested();
        int restType = NWScript.getLastRestEventType();

        if(restType != RestEventtype.REST_STARTED ||
                oPC.equals(NWObject.INVALID) ||
                NWScript.getIsDM(oPC)) return;

        Scheduler.assignNow(oPC, () -> NWScript.clearAllActions(false));
        DialogManager.startConversation(oPC, oPC, "RestMenu");
	}
}
