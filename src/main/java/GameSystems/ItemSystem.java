package GameSystems;

import Bioware.AddItemPropertyPolicy;
import Bioware.XP2;
import Data.Repository.ItemRepository;
import Entities.ItemEntity;
import GameObject.ItemGO;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.IpConstOnhitCastspell;
import org.nwnx.nwnx2.jvm.constants.ItemProperty;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.Arrays;

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
        ItemGO itemGO = new ItemGO(examinedObject);
        ApplyItemFeatures(examinedObject);
        String description = "";

        if(itemGO.getRecommendedLevel() > 0)
        {
            description += ColorToken.Orange() + "Recommended Level: " + ColorToken.End() + itemGO.getRecommendedLevel() + "\n";
        }
        if(itemGO.getAC() > 0)
        {
            description += ColorToken.Orange() + "AC: " + ColorToken.End() + itemGO.getRecommendedLevel() + "\n";
        }

        return existingDescription + "\n" + description;
    }

    public static void OnModuleEquipItem()
    {
        Integer[] validItemTypes = {
                BaseItem.ARMOR,
                BaseItem.ARROW,
                BaseItem.BASTARDSWORD,
                BaseItem.BATTLEAXE,
                BaseItem.BELT,
                BaseItem.BOLT,
                BaseItem.BOOTS,
                BaseItem.BRACER,
                BaseItem.BULLET,
                BaseItem.CLOAK,
                BaseItem.CLUB,
                BaseItem.DAGGER,
                BaseItem.DART,
                BaseItem.DIREMACE,
                BaseItem.DOUBLEAXE,
                BaseItem.DWARVENWARAXE,
                BaseItem.GLOVES,
                BaseItem.GREATAXE,
                BaseItem.GREATSWORD,
                BaseItem.GRENADE,
                BaseItem.HALBERD,
                BaseItem.HANDAXE,
                BaseItem.HEAVYCROSSBOW,
                BaseItem.HEAVYFLAIL,
                BaseItem.HELMET,
                BaseItem.KAMA,
                BaseItem.KATANA,
                BaseItem.KUKRI,
                BaseItem.LARGESHIELD,
                BaseItem.LIGHTCROSSBOW,
                BaseItem.LIGHTFLAIL,
                BaseItem.LIGHTHAMMER,
                BaseItem.LIGHTMACE,
                BaseItem.LONGBOW,
                BaseItem.LONGSWORD,
                BaseItem.MORNINGSTAR,
                BaseItem.QUARTERSTAFF,
                BaseItem.RAPIER,
                BaseItem.SCIMITAR,
                BaseItem.SCYTHE,
                BaseItem.SHORTBOW,
                BaseItem.SHORTSPEAR,
                BaseItem.SHORTSWORD,
                BaseItem.SHURIKEN,
                BaseItem.SICKLE,
                BaseItem.SLING,
                BaseItem.SMALLSHIELD,
                BaseItem.THROWINGAXE,
                BaseItem.TOWERSHIELD,
                BaseItem.TRIDENT,
                BaseItem.TWOBLADEDSWORD,
                BaseItem.WARHAMMER,
                BaseItem.WHIP
        };

        NWObject oItem = NWScript.getPCItemLastEquipped();
        Integer baseItemType = NWScript.getBaseItemType(oItem);

        if(!Arrays.asList(validItemTypes).contains(baseItemType)) return;

        for(NWItemProperty ip : NWScript.getItemProperties(oItem))
        {
            if(NWScript.getItemPropertyType(ip) == ItemProperty.ONHITCASTSPELL)
            {
                if(NWScript.getItemPropertySubType(ip) == IpConstOnhitCastspell.ONHIT_UNIQUEPOWER)
                {
                    return;
                }
            }
        }

        // No item property found. Add it to the item.
        XP2.IPSafeAddItemProperty(oItem, NWScript.itemPropertyOnHitCastSpell(IpConstOnhitCastspell.ONHIT_UNIQUEPOWER, 40), 0.0f, AddItemPropertyPolicy.ReplaceExisting, false, false);

    }

    private static void ApplyItemFeatures(NWObject item)
    {
        ItemEntity entity = GetItemEntity(item);
        if(entity == null) return;

        ItemGO itemGO = new ItemGO(item);

        itemGO.setAC(entity.getAc());
        itemGO.setCustomItemType(entity.getItemType().getItemTypeID());
        itemGO.setRecommendedLevel(entity.getRecommendedLevel());
    }
}
