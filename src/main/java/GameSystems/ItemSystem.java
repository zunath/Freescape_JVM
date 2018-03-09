package GameSystems;

import Data.Repository.ItemRepository;
import Entities.ItemEntity;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

public class ItemSystem {

    private static ItemEntity GetItemEntity(NWObject item)
    {
        String resref = NWScript.getResRef(item);
        ItemRepository repo = new ItemRepository();
        return repo.GetItemByResref(resref);
    }

    public static void OnItemAcquired()
    {
        NWObject item = NWScript.getModuleItemAcquired();
        ApplyItemFeatures(item);
    }

    public static String OnModuleExamine(String existingDescription, NWObject examiner, NWObject examinedObject)
    {
        if(!NWScript.getIsPC(examiner)) return existingDescription;
        if(NWScript.getObjectType(examinedObject) != ObjectType.ITEM) return existingDescription;
        ApplyItemFeatures(examinedObject);
        String description = "";

        int ac = NWScript.getLocalInt(examinedObject, "CUSTOM_ITEM_PROPERTY_AC");
        if(ac > 0)
        {
            description += ColorToken.Orange() + "AC: " + ColorToken.End() + ac + "\n";
        }

        return existingDescription + "\n" + description;
    }

    private static void ApplyItemFeatures(NWObject item)
    {
        ItemEntity entity = GetItemEntity(item);
        if(entity == null) return;

        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_AC", entity.getAc());
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE", entity.getItemType().getItemTypeID());
    }
}
