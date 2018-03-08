package GameSystems;

import Entities.StorageContainerEntity;
import Entities.StorageItemEntity;
import Helper.ColorToken;
import Data.Repository.StorageRepository;
import org.nwnx.nwnx2.jvm.*;

public class StorageSystem {

    public static void OnChestOpened(NWObject oChest)
    {
        NWObject oArea = NWScript.getArea(oChest);
        int containerID = NWScript.getLocalInt(oChest, "STORAGE_CONTAINER_ID");
        if(containerID <= 0) return;

        StorageRepository repo = new StorageRepository();
        StorageContainerEntity entity = repo.GetByContainerID(containerID);
        NWLocation chestLocation = NWScript.getLocation(oChest);
        boolean chestLoaded = NWScript.getLocalInt(oChest, "STORAGE_CONTAINER_LOADED") == 1;

        if(chestLoaded) return;

        if(entity == null)
        {
            entity = new StorageContainerEntity();
            entity.setAreaName(NWScript.getName(oArea, false));
            entity.setAreaResref(NWScript.getResRef(oArea));
            entity.setAreaTag(NWScript.getTag(oArea));
            entity.setStorageContainerID(containerID);

            repo.Save(entity);
        }

        for(StorageItemEntity item : entity.getStorageItems())
        {
            SCORCO.loadObject(item.getItemObject(), chestLocation, oChest);
        }

        NWScript.setLocalInt(oChest, "STORAGE_CONTAINER_LOADED", 1);
    }

    public static void OnChestDisturbed(NWObject oChest)
    {
        int containerID = NWScript.getLocalInt(oChest, "STORAGE_CONTAINER_ID");
        if(containerID <= 0) return;

        final NWObject oPC = NWScript.getLastDisturbed();
        StorageRepository repo = new StorageRepository();
        StorageContainerEntity entity = repo.GetByContainerID(containerID);
        entity.getStorageItems().clear();

        int itemCount = 0;
        int itemLimit = NWScript.getLocalInt(oChest, "STORAGE_CONTAINER_ITEM_LIMIT");
        boolean reachedLimit = false;

        if(itemLimit <= 0) itemLimit = 20;

        for(final NWObject item : NWScript.getItemsInInventory(oChest))
        {
            itemCount++;

            if(itemCount <= itemLimit)
            {
                StorageItemEntity itemEntity = new StorageItemEntity();
                itemEntity.setItemName(NWScript.getName(item, false));
                itemEntity.setItemTag(NWScript.getTag(item));
                itemEntity.setItemResref(NWScript.getResRef(item));
                itemEntity.setItemObject(SCORCO.saveObject(item));
                itemEntity.setStorageContainer(entity);

                entity.getStorageItems().add(itemEntity);
            }
            else
            {
                reachedLimit = true;

                Scheduler.assign(oChest, () -> NWScript.actionGiveItem(item, oPC));
            }
        }


        if(reachedLimit)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "No more items can be placed inside." + ColorToken.End());
        }
        else
        {
            NWScript.sendMessageToPC(oPC, ColorToken.White() + "Item Limit: "  + itemCount + " / " + ColorToken.End() + ColorToken.Red() + itemLimit + ColorToken.End());
        }

        repo.Save(entity);
    }

}
