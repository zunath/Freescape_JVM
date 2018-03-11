package Event.Module;
import Common.IScriptEventHandler;
import GameSystems.ItemSystem;
import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

@SuppressWarnings("UnusedDeclaration")
public class OnActivateItem implements IScriptEventHandler {
	@Override
	public void runScript(final NWObject objSelf) {

        NWScript.executeScript("x2_mod_def_act", objSelf);
		HandleGeneralItemUses();
		ItemSystem.OnModuleActivatedItem();
	}

	private void HandleGeneralItemUses()
	{
		NWObject oPC = NWScript.getItemActivator();
		NWObject oItem = NWScript.getItemActivated();

		String className = NWScript.getLocalString(oItem, "JAVA_SCRIPT");
		if(className.equals("")) return;

		Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));

		// Remove "Item." prefix if it exists.
		if(className.startsWith("Item."))
			className = className.substring(5);
		ScriptHelper.RunJavaScript(oPC, "Item." + className);
	}

}
