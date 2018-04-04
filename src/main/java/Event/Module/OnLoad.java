package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.*;
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
		FarmingSystem.OnModuleLoad();
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

    // Temporary while we're loading the blueprints into the DB.
    /*
    private void RunTest()
	{
		NWObject container = getObjectByTag("DBADD", 0);
		NWObject[] items = getItemsInInventory(container);
		SkillRepository skillRepo = new SkillRepository();
		CraftRepository craftRepo = new CraftRepository();

		SkillEntity skill = skillRepo.GetPCSkillByID("4007c4ae-ddf5-4a5f-ab8e-a24515a36457", SkillID.Weaponsmith).getSkill();

		CraftDeviceEntity device = new CraftDeviceEntity();
		device.setCraftDeviceID(2);
		device.setName("Weaponsmith Bench");

		CraftBlueprintCategoryEntity cat = new CraftBlueprintCategoryEntity();
		cat.setCraftBlueprintCategoryID(13);
		cat.setIsActive(true);
		cat.setName("Copper Weapons");


		int blueprintID = 101;
		for(NWObject item: items)
		{
			ItemGO itemGo = new ItemGO(item);

			CraftBlueprintEntity cb = new CraftBlueprintEntity();
			cb.setActive(true);
			cb.setCraftBlueprintID(blueprintID);
			cb.setCraftTierLevel(1);
			cb.setItemName(getName(item, false));
			cb.setItemResref(getResRef(item));
			cb.setQuantity(1);
			cb.setRequiredPerkLevel(1);
			cb.setSkill(skill);
			cb.setDevice(device);
			cb.setCategory(cat);
			cb.setLevel(1);

			ArrayList<CraftComponentEntity> components = new ArrayList<>();

			CraftComponentEntity c1 = new CraftComponentEntity();
			c1.setBlueprint(cb);
			CraftComponentEntity c2 = new CraftComponentEntity();
			c2.setBlueprint(cb);
			CraftComponentEntity c3 = new CraftComponentEntity();
			c3.setBlueprint(cb);
			CraftComponentEntity c4 = new CraftComponentEntity();
			c4.setBlueprint(cb);


			if(itemGo.IsBlade())
			{
				c1.setItemResref("elm_md_wpnhand");
				c1.setQuantity(1);

				c2.setItemResref("copper_ingot");
				c2.setQuantity(6);
			}
			else if(itemGo.IsFinesseBlade())
			{
				c1.setItemResref("elm_sm_wpnhand");
				c1.setQuantity(1);

				c2.setItemResref("copper_ingot");
				c2.setQuantity(4);

			}
			else if(itemGo.IsBlunt())
			{
				c1.setItemResref("elm_md_wpnhand");
				c1.setQuantity(1);

				c2.setItemResref("copper_ingot");
				c2.setQuantity(6);
			}
			else if(itemGo.IsHeavyBlade())
			{
				c1.setItemResref("elm_lg_wpnhand");
				c1.setQuantity(1);

				c2.setItemResref("copper_ingot");
				c2.setQuantity(8);
			}
			else if(itemGo.IsHeavyBlunt())
			{
				c1.setItemResref("elm_lg_wpnhand");
				c1.setQuantity(1);

				c2.setItemResref("copper_ingot");
				c2.setQuantity(8);
			}
			else if(itemGo.IsPolearm())
			{
				c1.setItemResref("elm_lg_wpnhand");
				c1.setQuantity(1);

				c2.setItemResref("copper_ingot");
				c2.setQuantity(6);
			}
			else if(itemGo.IsTwinBlade())
			{
				c1.setItemResref("elm_lg_wpnhand");
				c1.setQuantity(1);

				c2.setItemResref("copper_ingot");
				c2.setQuantity(8);
			}
			else if(itemGo.IsMartialArtsWeapon())
			{

			}
			else if(itemGo.IsBow())
			{

			}
			else if(itemGo.IsCrossbow())
			{

			}
			else if(itemGo.IsThrowing())
			{
				cb.setQuantity(25);

				c1.setItemResref("elm_sm_wpnhand");
				c1.setQuantity(1);

				c2.setItemResref("copper_ingot");
				c2.setQuantity(4);
			}

			if(c1.getItemResref() != null && !c1.getItemResref().equals(""))
				components.add(c1);
			if(c2.getItemResref() != null && !c2.getItemResref().equals(""))
				components.add(c2);
			if(c3.getItemResref() != null && !c3.getItemResref().equals(""))
				components.add(c3);
			if(c4.getItemResref() != null && !c4.getItemResref().equals(""))
				components.add(c4);

			System.out.println("Adding: " + getName(item, false));

			cb.setComponents(components);
			craftRepo.Save(cb);
			blueprintID++;
		}
	}
	*/
}
