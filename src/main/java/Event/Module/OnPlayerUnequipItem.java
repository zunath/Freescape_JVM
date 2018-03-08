package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.ArmorSystem;
import GameSystems.CombatSystem;
import GameSystems.InventorySystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnPlayerUnequipItem implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		CombatSystem combatSystem = new CombatSystem();

		// Bioware Default
		NWScript.executeScript("x2_mod_def_unequ", objSelf);
		combatSystem.OnModuleUnequip();
		ArmorSystem.OnModuleUnequipItem();
		InventorySystem.OnModuleUnEquipItem();

	}
}
