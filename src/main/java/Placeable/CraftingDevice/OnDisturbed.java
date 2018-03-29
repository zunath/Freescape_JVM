package Placeable.CraftingDevice;

import Common.IScriptEventHandler;
import Data.Repository.CraftRepository;
import Dialog.DialogManager;
import Entities.CraftBlueprintEntity;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import GameSystems.CraftSystem;
import Helper.ColorToken;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("unused")
public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject device) {
        CraftRepository craftRepo = new CraftRepository();
        NWObject oPC = getLastDisturbed();
        NWObject oItem = getInventoryDisturbItem();
        ItemGO itemGO = new ItemGO(oItem);
        int type = getInventoryDisturbType();
        String resref = getResRef(oItem);
        String tag = getTag(oItem);
        int blueprintID = getLocalInt(device, "CRAFT_BLUEPRINT_ID");
        int deviceID = getLocalInt(device, "CRAFT_DEVICE_ID");
        NWObject tools = getLocalObject(device, "CRAFT_DEVICE_TOOLS");
        CraftBlueprintEntity blueprint = craftRepo.GetBlueprintByID(blueprintID);

        if(type == InventoryDisturbType.REMOVED)
        {
            if(resref.equals("cft_choose_bp") || resref.equals("cft_craft_item"))
            {
                ItemHelper.ReturnItem(device, oItem);

                if(tag.equals("CHOOSE_BLUEPRINT"))
                {
                    DialogManager.startConversation(oPC, device, "Crafting");
                }
                else if(tag.equals("CRAFT_ITEM"))
                {
                    HandleCraftItem(oPC, device);
                }
            }
            else if(oItem.equals(tools))
            {
                deleteLocalObject(device, "CRAFT_DEVICE_TOOLS");
            }
        }
        else if(type == InventoryDisturbType.ADDED)
        {
            if(resref.equals("cft_choose_bp") || resref.equals("cft_craft_item")) return;

            if(blueprint == null)
            {
                ItemHelper.ReturnItem(oPC, oItem);
                sendMessageToPC(oPC, "Please select a blueprint before adding components.");
            }
            else if(itemGO.getCraftTierLevel() > 0)
            {
                if(blueprint.getCraftTierLevel() <= 0)
                {
                    ItemHelper.ReturnItem(oPC, oItem);
                    sendMessageToPC(oPC, "Tools are not required to make this item.");
                }
                else if(blueprint.getCraftTierLevel() > itemGO.getCraftTierLevel() || blueprint.getSkill().getSkillID() != itemGO.getAssociatedSkillID())
                {
                    ItemHelper.ReturnItem(oPC, oItem);
                    sendMessageToPC(oPC, "Those tools cannot be used with this blueprint. (Required Tool Level: " + blueprint.getCraftTierLevel() + ")");
                }
                else
                {
                    if(getIsObjectValid(tools))
                    {
                        ItemHelper.ReturnItem(oPC, oItem);
                        sendMessageToPC(oPC, "You may only use one set of tools at a time.");
                    }
                    else
                    {
                        setLocalObject(device, "CRAFT_DEVICE_TOOLS", oItem);
                    }
                }
            }
        }

    }

    private void HandleCraftItem(NWObject oPC, NWObject device)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        CraftRepository repo = new CraftRepository();
        int blueprintID = getLocalInt(device, "CRAFT_BLUEPRINT_ID");
        int deviceID = getLocalInt(device, "CRAFT_DEVICE_ID");
        CraftBlueprintEntity pcBlueprint = repo.GetBlueprintKnownByPC(pcGO.getUUID(), blueprintID, deviceID);

        if(pcBlueprint == null)
        {
            floatingTextStringOnCreature(ColorToken.Red() + "You do not know that blueprint." + ColorToken.End(), oPC, false);
            return;
        }

        CraftSystem.CraftItem(oPC, device, blueprintID);
    }

}
