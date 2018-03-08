package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.KeyItemSystem;
import GameSystems.QuestSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnAcquireItem implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		// Bioware Default
		NWScript.executeScript("x2_mod_def_aqu", objSelf);
        KeyItemSystem.OnModuleItemAcquired();
		QuestSystem.OnItemAcquired();
	}
}
