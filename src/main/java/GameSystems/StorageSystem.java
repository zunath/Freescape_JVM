package GameSystems;

import Data.Repository.StorageRepository;
import Entities.StorageContainerEntity;
import Entities.StorageItemEntity;
import GameObject.ItemGO;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.SCORCO;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class StorageSystem {

    public static void OnChestOpened(NWObject oChest)
    {
        NWObject oArea = getArea(oChest);
        int containerID = getLocalInt(oChest, "STORAGE_CONTAINER_ID");
        if(containerID <= 0) return;

        StorageRepository repo = new StorageRepository();
        StorageContainerEntity entity = repo.GetByContainerID(containerID);
        NWLocation chestLocation = getLocation(oChest);
        boolean chestLoaded = getLocalInt(oChest, "STORAGE_CONTAINER_LOADED") == 1;

        if(chestLoaded) return;

        if(entity == null)
        {
            entity = new StorageContainerEntity();
            entity.setAreaName(getName(oArea, false));
            entity.setAreaResref(getResRef(oArea));
            entity.setAreaTag(getTag(oArea));
            entity.setStorageContainerID(containerID);

            repo.Save(entity);
        }

        for(StorageItemEntity item : entity.getStorageItems())
        {
            SCORCO.loadObject(item.getItemObject(), chestLocation, oChest);
        }

        setLocalInt(oChest, "STORAGE_CONTAINER_LOADED", 1);
    }

    public static void OnChestDisturbed(NWObject oChest)
    {
        int containerID = getLocalInt(oChest, "STORAGE_CONTAINER_ID");
        if(containerID <= 0) return;

        NWObject oPC = getLastDisturbed();
        NWObject oItem = getInventoryDisturbItem();
        ItemGO itemGO = new ItemGO(oItem);
        int disturbType = getInventoryDisturbType();
        int itemCount = CountItems(oChest);
        int itemLimit = getLocalInt(oChest, "STORAGE_CONTAINER_ITEM_LIMIT");
        if(itemLimit <= 0) itemLimit = 20;

        StorageRepository repo = new StorageRepository();
        StorageContainerEntity entity = repo.GetByContainerID(containerID);

        if(disturbType == InventoryDisturbType.ADDED)
        {
            if(itemCount > itemLimit)
            {
                ReturnItem(oPC, oItem);
                sendMessageToPC(oPC, ColorToken.Red() + "No more items can be placed inside." + ColorToken.End());
            }
            else
            {
                StorageItemEntity itemEntity = new StorageItemEntity();
                itemEntity.setItemName(getName(oItem, false));
                itemEntity.setItemTag(getTag(oItem));
                itemEntity.setItemResref(getResRef(oItem));
                itemEntity.setGlobalID(itemGO.getUUID());
                itemEntity.setItemObject(SCORCO.saveObject(oItem));
                itemEntity.setStorageContainer(entity);

                entity.getStorageItems().add(itemEntity);
                repo.Save(entity);
            }
        }
        else if(disturbType == InventoryDisturbType.REMOVED)
        {
            repo.DeleteStorageItemByGlobalID(itemGO.getUUID());
        }

        sendMessageToPC(oPC, ColorToken.White() + "Item Limit: "  + itemCount + " / " + ColorToken.End() + ColorToken.Red() + itemLimit + ColorToken.End());
    }

    private static void ReturnItem(NWObject oPC, NWObject oItem)
    {
        copyItem(oItem, oPC, true);
        destroyObject(oItem, 0.0f);
    }

    private static int CountItems(NWObject container)
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
