package Helper;

import Enumerations.CustomBaseItemType;
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
                BaseItem.TWOBLADEDSWORD,
                CustomBaseItemType.LightPick,
                CustomBaseItemType.HeavyPick
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

    public static boolean IsFirearm(NWObject item)
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                CustomBaseItemType.HeavyWeapon,
                CustomBaseItemType.Longarm,
                CustomBaseItemType.SmallArmD6,
                CustomBaseItemType.SmallArmD6_2,
                CustomBaseItemType.SmallArmD8,
                CustomBaseItemType.D20HeavyWeapon,
                CustomBaseItemType.D20SmallArms6,
                CustomBaseItemType.MZS3Handgun,
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

    public static boolean IsArmor(NWObject item)
    {
        int itemType = NWScript.getBaseItemType(item);
        return itemType == BaseItem.BELT;
    }

}
