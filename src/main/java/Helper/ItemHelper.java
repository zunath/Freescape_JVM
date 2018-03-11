package Helper;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;

import java.util.Arrays;

public class ItemHelper {

    public static void ReduceItemStack(NWObject item)
    {
        int stackSize = NWScript.getItemStackSize(item);
        if(stackSize > 1)
        {
            NWScript.setItemStackSize(item, stackSize-1);
        }
        else
        {
            NWScript.destroyObject(item, 0.0f);
        }
    }

    public static void ReduceItemCharges(NWObject item)
    {
        NWScript.setItemCharges(item, NWScript.getItemCharges(item)-1);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean IsBlade(NWObject item)
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.BASTARDSWORD,
                BaseItem.BATTLEAXE,
                BaseItem.DOUBLEAXE,
                BaseItem.DWARVENWARAXE,
                BaseItem.GREATAXE,
                BaseItem.GREATSWORD,
                BaseItem.HALBERD,
                BaseItem.DAGGER,
                BaseItem.HANDAXE,
                BaseItem.KAMA,
                BaseItem.KATANA,
                BaseItem.KUKRI,
                BaseItem.LONGSWORD,
                BaseItem.RAPIER,
                BaseItem.SCIMITAR,
                BaseItem.SCYTHE,
                BaseItem.SHORTSWORD,
                BaseItem.TWOBLADEDSWORD
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public static boolean IsBlunt(NWObject item)
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.CLUB,
                BaseItem.DIREMACE,
                BaseItem.HEAVYFLAIL,
                BaseItem.LIGHTFLAIL,
                BaseItem.LIGHTHAMMER,
                BaseItem.LIGHTMACE,
                BaseItem.MORNINGSTAR,
                BaseItem.QUARTERSTAFF,
                BaseItem.WARHAMMER,
                BaseItem.WHIP
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public static boolean IsRanged(NWObject item)
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.HEAVYCROSSBOW,
                BaseItem.LIGHTCROSSBOW,
                BaseItem.LONGBOW,
                BaseItem.SHORTBOW
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public static boolean IsThrowing(NWObject item)
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.THROWINGAXE,
                BaseItem.SHURIKEN,
                BaseItem.DART
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

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
}
