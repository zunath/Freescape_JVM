package GameObject;

import Common.Constants;
import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;

import java.util.Arrays;
import java.util.UUID;

import static org.nwnx.nwnx2.jvm.NWScript.*;

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


    public String getUUID()
    {
        String uuid = getLocalString(item, "GLOBAL_ID");

        if(uuid.equals(""))
        {
            uuid = UUID.randomUUID().toString();
            setLocalString(item, "GLOBAL_ID", uuid);
        }

        return uuid;
    }

    public void stripAllItemProperties()
    {
        for(NWItemProperty prop : getItemProperties(item))
        {
            removeItemProperty(item, prop);
        }
    }

    public boolean hasItemProperty(int itemPropertyID)
    {
        boolean hasItemProperty = false;
        for(NWItemProperty ip : getItemProperties(item))
        {
            if(getItemPropertyType(ip) == itemPropertyID)
            {
                hasItemProperty = true;
                break;
            }
        }

        return hasItemProperty;
    }

    public void setAC(int ac)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_AC", ac);
    }

    public int getAC()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_AC");
    }

    public int getCustomItemType()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE");
    }

    public void setCustomItemType(int itemType)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE", itemType);
    }

    public int getRecommendedLevel()
    {
        return getLocalInt(item , "CUSTOM_ITEM_PROPERTY_TYPE_RECOMMENDED_LEVEL");
    }

    public void setRecommendedLevel(int recommendedSkillLevel)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE_RECOMMENDED_LEVEL", recommendedSkillLevel);
    }

    public int getLoggingBonus()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_LOGGING_BONUS");
    }

    public void setLoggingBonus(int loggingBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_LOGGING_BONUS", loggingBonus);
    }

    public int getMiningBonus()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_MINING_BONUS");
    }

    public void setMiningBonus(int miningBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_MINING_BONUS", miningBonus);
    }

    public int getCastingSpeed()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_CASTING_SPEED");
    }

    public void setCastingSpeed(int castingSpeed)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_CASTING_SPEED", castingSpeed);
    }

    public int getCraftBonusMetalworking()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_METALWORKING");
    }

    public void setCraftBonusMetalworking(int craftBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_METALWORKING", craftBonus);
    }

    public int getCraftBonusArmorsmith()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_ARMORSMITH");
    }

    public void setCraftBonusArmorsmith(int craftBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_ARMORSMITH", craftBonus);
    }

    public int getCraftBonusWeaponsmith()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_WEAPONSMITH");
    }

    public void setCraftBonusWeaponsmith(int craftBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_WEAPONSMITH", craftBonus);
    }

    public int getCraftBonusCooking()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_COOKING");
    }

    public void setCraftBonusCooking(int craftBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_COOKING", craftBonus);
    }

    public int getCraftBonusWoodworking()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_WOODWORKING");
    }

    public void setCraftBonusWoodworking(int craftBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_BONUS_WOODWORKING", craftBonus);
    }

    public int getAssociatedSkillID()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_ASSOCIATED_SKILL_ID");
    }

    public void setAssociatedSkillID(int associatedSkillID)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_ASSOCIATED_SKILL_ID", associatedSkillID);
    }

    public int getCraftTierLevel()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_TIER_LEVEL");
    }

    public void setCraftTierLevel(int craftTierLevel)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_CRAFT_TIER_LEVEL", craftTierLevel);
    }

    public int getHPBonus()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_HP_BONUS");
    }

    public void setHPBonus(int hpBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_HP_BONUS", hpBonus);
    }

    public int getManaBonus()
    {
        return getLocalInt(item, "CUSTOM_ITEM_PROPERTY_MANA_BONUS");
    }

    public void setManaBonus(int manaBonus)
    {
        setLocalInt(item, "CUSTOM_ITEM_PROPERTY_MANA_BONUS", manaBonus);
    }

    public void ReduceItemStack()
    {
        int stackSize = getItemStackSize(item);
        if(stackSize > 1)
        {
            setItemStackSize(item, stackSize-1);
        }
        else
        {
            destroyObject(item, 0.0f);
        }
    }

    public void ReduceItemCharges()
    {
        setItemCharges(item, getItemCharges(item)-1);
    }

    public boolean IsBlade()
    {
        int itemType = getBaseItemType(item);

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
        int itemType = getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.DAGGER,
                BaseItem.HANDAXE,
                BaseItem.KUKRI,
                BaseItem.RAPIER,
                BaseItem.SCIMITAR,
                BaseItem.SICKLE,
                BaseItem.SHORTSWORD,
                BaseItem.WHIP
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsBlunt()
    {
        int itemType = getBaseItemType(item);

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
        int itemType = getBaseItemType(item);

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
        int itemType = getBaseItemType(item);

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
        int itemType = getBaseItemType(item);

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
        int itemType = getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.DOUBLEAXE,
                BaseItem.TWOBLADEDSWORD
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsMartialArtsWeapon()
    {
        int itemType = getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.GLOVES,
                BaseItem.BRACER,
                BaseItem.KAMA
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }


    public boolean IsBow()
    {
        int itemType = getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.LONGBOW,
                BaseItem.SHORTBOW
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsCrossbow()
    {
        int itemType = getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.HEAVYCROSSBOW,
                BaseItem.LIGHTCROSSBOW
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }

    public boolean IsThrowing()
    {
        int itemType = getBaseItemType(item);

        Integer[] allowedWeaponTypes = {
                BaseItem.THROWINGAXE,
                BaseItem.SHURIKEN,
                BaseItem.DART,
                BaseItem.SLING
        };

        return Arrays.asList(allowedWeaponTypes).contains(itemType);
    }
}
