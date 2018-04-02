package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.ItemSystem;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnPlayerUnequipItem implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		// Bioware Default
		NWScript.executeScript("x2_mod_def_unequ", objSelf);
		SkillSystem.OnModuleItemUnequipped();
		PerkSystem.OnModuleItemUnequipped();
	}
}
