package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.AreaInstanceSystem;
import GameSystems.DeathSystem;
import GameSystems.ObjectProcessingSystem;
import GameSystems.StructureSystem;
import NWNX.EventType;
import NWNX.NWNX_Chat;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.EventScript;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("unused")
public class OnLoad implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWNX_Chat.RegisterChatScript("mod_on_nwnxchat");
        AddGlobalEventHandlers();
		AddAreaEventHandlers();

		// Bioware default
		executeScript("x2_mod_def_load", objSelf);
        DeathSystem.OnModuleLoad();
		StructureSystem.OnModuleLoad();
		AreaInstanceSystem.OnModuleLoad();
		ObjectProcessingSystem.OnModuleLoad();
	}

	private void AddAreaEventHandlers()
	{
		NWObject area = getFirstArea();
		while(getIsObjectValid(area))
		{
			setEventScript(area, EventScript.AREA_ON_ENTER, "area_enter");
			setEventScript(area, EventScript.AREA_ON_EXIT, "area_exit");
			setEventScript(area, EventScript.AREA_ON_HEARTBEAT, "area_heartbeat");
			setEventScript(area, EventScript.AREA_ON_USER_DEFINED_EVENT, "area_user");

			area = getNextArea();
		}
	}

    private void AddGlobalEventHandlers()
    {
        NWNX_Events.SubscribeEvent(EventType.StartCombatRoundBefore, "mod_on_attack");
        NWNX_Events.SubscribeEvent(EventType.ExamineObjectBefore, "mod_on_examine");
        NWNX_Events.SubscribeEvent(EventType.UseFeatBefore, "mod_on_usefeat");
        NWNX_Events.SubscribeEvent(EventType.UseItemBefore, "mod_on_useitem");


    }
}
