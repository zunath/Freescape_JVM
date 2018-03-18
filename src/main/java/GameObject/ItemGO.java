package GameObject;

import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;

import java.util.Arrays;

public class ItemGO {
    private String tag;
    private String resref;
    private String script;
    private NWObject item;

    public ItemGO(NWObject oItem)
    {
        this.item = oItem;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void stripAllItemProperties()
    {
        for(NWItemProperty prop : NWScript.getItemProperties(item))
        {
            NWScript.removeItemProperty(item, prop);
        }
    }

    public boolean hasItemProperty(int itemPropertyID)
    {
        boolean hasItemProperty = false;
        for(NWItemProperty ip : NWScript.getItemProperties(item))
        {
            if(NWScript.getItemPropertyType(ip) == itemPropertyID)
            {
                hasItemProperty = true;
                break;
            }
        }

        return hasItemProperty;
    }

    public void setAC(int ac)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_AC", ac);
    }

    public int getAC()
    {
        return NWScript.getLocalInt(item, "CUSTOM_ITEM_PROPERTY_AC");
    }

    public int getCustomItemType()
    {
        return NWScript.getLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE");
    }

    public void setCustomItemType(int itemType)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE", itemType);
    }

    public int getRecommendedLevel()
    {
        return NWScript.getLocalInt(item , "CUSTOM_ITEM_PROPERTY_TYPE_RECOMMENDED_LEVEL");
    }

    public void setRecommendedLevel(int recommendedSkillLevel)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE_RECOMMENDED_LEVEL", recommendedSkillLevel);
    }

    public int getLoggingBonus()
    {
        return NWScript.getLocalInt(item, "CUSTOM_ITEM_PROPERTY_LOGGING_BONUS");
    }

    public void setLoggingBonus(int loggingBonus)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_LOGGING_BONUS", loggingBonus);
    }

    public int getMiningBonus()
    {
        return NWScript.getLocalInt(item, "CUSTOM_ITEM_PROPERTY_MINING_BONUS");
    }

    public void setMiningBonus(int miningBonus)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_MINING_BONUS", miningBonus);
    }

    public int getCastingSpeed()
    {
        return NWScript.getLocalInt(item, "CUSTOM_ITEM_PROPERTY_CASTING_SPEED");
    }

    public void setCastingSpeed(int castingSpeed)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_CASTING_SPEED", castingSpeed);
    }

    public void ReduceItemStack()
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

    public void ReduceItemCharges()
    {
        NWScript.setItemCharges(item, NWScript.getItemCharges(item)-1);
    }

    public boolean IsBlade()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.BASTARDSWORD,
                BaseItem.KATANA,
                BaseItem.LONGSWORD,
                BaseItem.SCIMITAR
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsFinesseBlade()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.DAGGER,
                BaseItem.HANDAXE,
                BaseItem.KUKRI,
                BaseItem.RAPIER,
                BaseItem.SCIMITAR,
                BaseItem.SHORTSWORD,
                BaseItem.WHIP
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsBlunt()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.CLUB,
                BaseItem.LIGHTFLAIL,
                BaseItem.LIGHTHAMMER,
                BaseItem.LIGHTMACE,
                BaseItem.MORNINGSTAR
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsHeavyBlade()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.BATTLEAXE,
                BaseItem.DWARVENWARAXE,
                BaseItem.GREATAXE,
                BaseItem.GREATSWORD
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsHeavyBlunt()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.HEAVYFLAIL,
                BaseItem.WARHAMMER,
                BaseItem.DIREMACE,
                BaseItem.QUARTERSTAFF
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsPolearm()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.HALBERD,
                BaseItem.SCYTHE,
                BaseItem.SHORTSPEAR,
                BaseItem.TRIDENT
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }
    public boolean IsTwinBlade()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.DOUBLEAXE,
                BaseItem.TWOBLADEDSWORD
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsMartialArtsWeapon()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.GLOVES,
                BaseItem.BRACER,
                BaseItem.KAMA
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }


    public boolean IsBow()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.LONGBOW,
                BaseItem.SHORTBOW
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsCrossbow()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.HEAVYCROSSBOW,
                BaseItem.LIGHTCROSSBOW
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsThrowing()
    {
        int itemType = NWScript.getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.THROWINGAXE,
                BaseItem.SHURIKEN,
                BaseItem.DART,
                BaseItem.SLING
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }
}
