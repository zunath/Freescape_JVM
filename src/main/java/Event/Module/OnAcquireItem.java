package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.InventorySystem;
import GameSystems.KeyItemSystem;
import GameSystems.QuestSystem;
import GameSystems.RadioSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnAcquireItem implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		RadioSystem radioSystem = new RadioSystem();

		// Bioware Default
		NWScript.executeScript("x2_mod_def_aqu", objSelf);
		radioSystem.OnModuleAcquire();
		InventorySystem.OnModuleAcquireItem();
        KeyItemSystem.OnModuleItemAcquired();
		QuestSystem.OnItemAcquired();
	}
}
