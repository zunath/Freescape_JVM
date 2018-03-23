package Placeable.CraftingDevice;

import Dialog.DialogManager;
import Entities.CraftBlueprintEntity;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Common.IScriptEventHandler;
import Data.Repository.CraftRepository;
import GameSystems.CraftSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;

@SuppressWarnings("unused")
public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject device) {
        NWObject oPC = NWScript.getLastDisturbed();
        NWObject oItem = NWScript.getInventoryDisturbItem();
        int type = NWScript.getInventoryDisturbType();
        String resref = NWScript.getResRef(oItem);
        String tag = NWScript.getTag(oItem);

        if(type == InventoryDisturbType.REMOVED)
        {
            if(resref.equals("cft_choose_bp") || resref.equals("cft_craft_item"))
            {
                NWScript.copyItem(oItem, device, true);
                NWScript.destroyObject(oItem, 0.0f);

                if(tag.equals("CHOOSE_BLUEPRINT"))
                {
                    DialogManager.startConversation(oPC, device, "Crafting");
                }
                else if(tag.equals("CRAFT_ITEM"))
                {
                    HandleCraftItem(oPC, device);
                }
            }
        }

    }

    private void HandleCraftItem(NWObject oPC, NWObject device)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        CraftRepository repo = new CraftRepository();
        int blueprintID = NWScript.getLocalInt(device, "CRAFT_BLUEPRINT_ID");
        int deviceID = NWScript.getLocalInt(device, "CRAFT_DEVICE_ID");
        CraftBlueprintEntity pcBlueprint = repo.GetBlueprintKnownByPC(pcGO.getUUID(), blueprintID, deviceID);

        if(pcBlueprint == null)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You do not know that blueprint." + ColorToken.End(), oPC, false);
            return;
        }

        CraftSystem.CraftItem(oPC, device, blueprintID);
    }

}
