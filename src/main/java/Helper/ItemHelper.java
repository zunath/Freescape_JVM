package Helper;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import static org.nwnx.nwnx2.jvm.NWScript.copyItem;
import static org.nwnx.nwnx2.jvm.NWScript.destroyObject;

public class ItemHelper {

    public static String GetNameByResref(String itemResref)
    {
        NWObject tempStorage = NWScript.getObjectByTag("TEMP_ITEM_STORAGE", 0);
        if(!NWScript.getIsObjectValid(tempStorage))
        {
            System.out.println("Could not locate temp item storage object. Create a placeable container in a non-accessible area with the tag TEMP_ITEM_STORAGE.");
            return null;
        }
        NWObject item = NWScript.createItemOnObject(itemResref, tempStorage, 1, "");
        String name = NWScript.getName(item, false);
        NWScript.destroyObject(item, 0.0f);
        return name;
    }

    public static void ReturnItem(NWObject oTarget, NWObject oItem)
    {
        copyItem(oItem, oTarget, true);
        destroyObject(oItem, 0.0f);
    }
}
