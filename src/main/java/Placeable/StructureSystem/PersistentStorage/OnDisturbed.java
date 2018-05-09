package Placeable.StructureSystem.PersistentStorage;

import Common.IScriptEventHandler;
import Data.Repository.StructureRepository;
import Entities.PCTerritoryFlagStructureEntity;
import Entities.PCTerritoryFlagStructureItemEntity;
import GameObject.ItemGO;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.SCORCO;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("unused")
public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject container) {
        NWObject oPC = getLastDisturbed();
        NWObject item = getInventoryDisturbItem();
        int disturbType = getInventoryDisturbType();

        ItemGO itemGO = new ItemGO(item);
        int structureID = getLocalInt(container, "STRUCTURE_TEMP_STRUCTURE_ID");
        StructureRepository repo = new StructureRepository();
        PCTerritoryFlagStructureEntity entity = repo.GetPCStructureByID(structureID);
        int itemCount = CountItems(container);
        String itemResref = getResRef(item);

        if(disturbType == InventoryDisturbType.ADDED)
        {
            if(itemCount > entity.getBlueprint().getItemStorageCount())
            {
                ReturnItem(oPC, item);
                sendMessageToPC(oPC, ColorToken.Red() + "No more items can be placed inside." + ColorToken.End());
            }
            // Only specific types of items can be stored in resource bundles
            else if(!entity.getBlueprint().getResourceResref().equals("") && !itemResref.equals(entity.getBlueprint().getResourceResref()))
            {
                ReturnItem(oPC, item);
                sendMessageToPC(oPC, ColorToken.Red() + "That item cannot be stored here." + ColorToken.End());
            }
            else
            {
                PCTerritoryFlagStructureItemEntity itemEntity = new PCTerritoryFlagStructureItemEntity();
                itemEntity.setItemName(getName(item, false));
                itemEntity.setItemResref(itemResref);
                itemEntity.setItemTag((getTag(item)));
                itemEntity.setStructure(entity);
                itemEntity.setGlobalID(itemGO.getUUID());
                itemEntity.setItemObject(SCORCO.saveObject(item));

                entity.getItems().add(itemEntity);
                repo.Save(entity);
            }
        }
        else if(disturbType == InventoryDisturbType.REMOVED)
        {
            repo.DeleteContainerItemByGlobalID(itemGO.getUUID());
        }

        sendMessageToPC(oPC, ColorToken.White() + "Item Limit: "  + itemCount + " / " + ColorToken.End() + ColorToken.Red() + entity.getBlueprint().getItemStorageCount() + ColorToken.End());

    }

    private void ReturnItem(NWObject oPC, NWObject oItem)
    {
        copyItem(oItem, oPC, true);
        destroyObject(oItem, 0.0f);
    }

    private int CountItems(NWObject container)
    {
        int count = 0;

        NWObject item = getFirstItemInInventory(container);
        while(getIsObjectValid(item))
        {
            count++;
            item = getNextItemInInventory(container);
        }

        return count;
    }

}
