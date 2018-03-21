package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.AreaInstanceSystem;
import GameSystems.ObjectProcessingSystem;
import NWNX.*;
import GameSystems.DeathSystem;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class OnLoad implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		NWNX_Chat.RegisterChatScript("mod_on_nwnxchat");
        AddGlobalEventHandlers();
		AddAreaEventHandlers();

		// Bioware default
		NWScript.executeScript("x2_mod_def_load", objSelf);
        DeathSystem.OnModuleLoad();
		StructureSystem.OnModuleLoad();
		AreaInstanceSystem.OnModuleLoad();
		ObjectProcessingSystem.OnModuleLoad();
	}

	private void AddAreaEventHandlers()
	{
		NWObject area = NWScript.getFirstArea();
		while(NWScript.getIsObjectValid(area))
		{
			NWNX_Object.SetEventHandler(area, AreaObjectScript.OnEnter, "area_enter");
			NWNX_Object.SetEventHandler(area, AreaObjectScript.OnExit, "area_exit");
			NWNX_Object.SetEventHandler(area, AreaObjectScript.OnHeartbeat, "area_heartbeat");
			NWNX_Object.SetEventHandler(area, AreaObjectScript.OnUserDefinedEvent, "area_user");

			area = NWScript.getNextArea();
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
