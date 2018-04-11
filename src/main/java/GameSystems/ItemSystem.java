package GameSystems;

import Bioware.AddItemPropertyPolicy;
import Bioware.XP2;
import Data.Repository.ItemRepository;
import Data.Repository.SkillRepository;
import Entities.ItemEntity;
import Entities.PCSkillEntity;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ScriptHelper;
import Item.IActionItem;
import NWNX.NWNX_Item;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

import java.lang.Object;
import java.util.Arrays;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class ItemSystem {

    private static ItemEntity GetItemEntity(NWObject item)
    {
        String resref = getResRef(item);
        ItemRepository repo = new ItemRepository();
        return repo.GetItemByResref(resref);
    }

    public static void OnItemAcquired()
    {
        NWObject oPC = getModuleItemAcquiredBy();

        if(!getIsPC(oPC)) return;

        NWObject item = getModuleItemAcquired();
        ApplyItemFeatures(item);
    }

    public static void OnModuleHeartbeat()
    {
        NWObject[] pcs = getPCs();
        for(NWObject pc: pcs)
        {
            NWObject offHand = getItemInSlot(InventorySlot.LEFTHAND, pc);
            int type = getBaseItemType(offHand);

            if(type == BaseItem.TORCH)
            {
                int charges = getItemCharges(offHand) - 1;
                if(charges <= 0)
                {
                    destroyObject(offHand, 0.0f);
                    sendMessageToPC(pc, "Your torch has burned out.");
                }
                else
                {
                    setItemCharges(offHand, charges);
                }
            }
        }
    }


    public static String OnModuleExamine(String existingDescription, NWObject examiner, NWObject examinedObject)
    {
        if(!getIsPC(examiner)) return existingDescription;
        if(getObjectType(examinedObject) != ObjectType.ITEM) return existingDescription;
        PlayerGO pcGO = new PlayerGO(examiner);
        SkillRepository skillRepo = new SkillRepository();
        ItemGO itemGO = new ItemGO(examinedObject);
        ApplyItemFeatures(examinedObject);
        String description = "";

        if(itemGO.getRecommendedLevel() > 0)
        {
            description += ColorToken.Orange() + "Recommended Level: " + ColorToken.End() + itemGO.getRecommendedLevel() + "\n";
        }
        if(itemGO.getAssociatedSkillID() > 0)
        {
            PCSkillEntity pcSkill = skillRepo.GetPCSkillByID(pcGO.getUUID(), itemGO.getAssociatedSkillID());
            description += ColorToken.Orange() + "Associated Skill: " + ColorToken.End() + pcSkill.getSkill().getName() + "\n";
        }
        if(itemGO.getAC() > 0)
        {
            description += ColorToken.Orange() + "AC: " + ColorToken.End() + itemGO.getAC() + "\n";
        }
        if(itemGO.getLoggingBonus() > 0)
        {
            description += ColorToken.Orange() + "Logging Bonus: " + ColorToken.End() + itemGO.getLoggingBonus() + "\n";
        }
        if(itemGO.getMiningBonus() > 0)
        {
            description += ColorToken.Orange() + "Mining Bonus: " + ColorToken.End() + itemGO.getMiningBonus() + "\n";
        }
        if(itemGO.getCraftBonusMetalworking() > 0)
        {
            description += ColorToken.Orange() + "Metalworking Bonus: " + ColorToken.End() + itemGO.getCraftBonusMetalworking() + "\n";
        }
        if(itemGO.getCraftBonusArmorsmith() > 0)
        {
            description += ColorToken.Orange() + "Armorsmith Bonus: " + ColorToken.End() + itemGO.getCraftBonusArmorsmith() + "\n";
        }
        if(itemGO.getCraftBonusWeaponsmith() > 0)
        {
            description += ColorToken.Orange() + "Weaponsmith Bonus: " + ColorToken.End() + itemGO.getCraftBonusWeaponsmith() + "\n";
        }
        if(itemGO.getCraftBonusCooking() > 0)
        {
            description += ColorToken.Orange() + "Cooking Bonus: " + ColorToken.End() + itemGO.getCraftBonusCooking() + "\n";
        }
        if(itemGO.getCraftTierLevel() > 0)
        {
            description += ColorToken.Orange() + "Tool Level: " + ColorToken.End() + itemGO.getCraftTierLevel() + "\n";
        }

        return existingDescription + "\n" + description;
    }

    public static void OnModuleActivatedItem()
    {
        NWObject user = getItemActivator();
        if(!getIsPC(user) || getIsDM(user) || getIsDMPossessed(user)) return;

        PlayerGO pcGO = new PlayerGO(user);
        NWObject target = getItemActivatedTarget();
        NWObject item = getItemActivated();
        String actionScript = getLocalString(item, "JAVA_ACTION_SCRIPT");
        if(actionScript.equals("")) return;

        IActionItem actionItem = (IActionItem) ScriptHelper.GetClassByName("Item." + actionScript);
        if(actionItem == null) return;

        if(pcGO.isBusy())
        {
            sendMessageToPC(user, "You are busy.");
            return;
        }

        String invalidTargetMessage = actionItem.IsValidTarget(user, item, target);
        if(invalidTargetMessage != null && !invalidTargetMessage.equals(""))
        {
            sendMessageToPC(user, invalidTargetMessage);
            return;
        }

        if(actionItem.MaxDistance() > 0.0f)
        {
            if(getDistanceBetween(user, target) > actionItem.MaxDistance() ||
                    !getResRef(getArea(user)).equals(getResRef(getArea(target))))
            {
                sendMessageToPC(user, "Your target is too far away.");
                return;
            }
        }

        Object customData = actionItem.StartUseItem(user, item, target);
        float delay = actionItem.Seconds(user, item, target, customData);
        int animationID = actionItem.AnimationID();
        boolean faceTarget = actionItem.FaceTarget();
        NWVector userPosition = getPosition(user);

        Scheduler.assign(user, () -> {
            pcGO.setIsBusy(true);
            if(faceTarget)
                setFacingPoint(getPosition(target));
            if(animationID > 0)
                actionPlayAnimation(animationID, 1.0f, delay);
        });

        NWNX_Player.StartGuiTimingBar(user, delay, "");
        Scheduler.delay(user, (int)(delay * 1000), () -> FinishActionItem(actionItem, user, item, target, userPosition, customData));
    }

    private static void FinishActionItem(IActionItem actionItem, NWObject user, NWObject item, NWObject target, NWVector userStartPosition, Object customData)
    {
        PlayerGO pcGO = new PlayerGO(user);
        pcGO.setIsBusy(false);

        NWVector userPosition = getPosition(user);
        if(!userPosition.equals(userStartPosition))
        {
            sendMessageToPC(user, "You move and interrupt your action.");
            return;
        }

        if(actionItem.MaxDistance() > 0.0f)
        {
            if(getDistanceBetween(user, target) > actionItem.MaxDistance() ||
                    !getResRef(getArea(user)).equals(getResRef(getArea(target))))
            {
                sendMessageToPC(user, "Your target is too far away.");
                return;
            }
        }

        if(!getIsObjectValid(target))
        {
            sendMessageToPC(user, "Unable to locate target.");
            return;
        }

        String invalidTargetMessage = actionItem.IsValidTarget(user, item, target);
        if(invalidTargetMessage != null && !invalidTargetMessage.equals(""))
        {
            sendMessageToPC(user, invalidTargetMessage);
            return;
        }

        actionItem.ApplyEffects(user, item, target, customData);

        ItemGO itemGO = new ItemGO(item);
        if(actionItem.ReducesItemCharge(user, item, target, customData))
        {
            if(getItemCharges(item) > 0) itemGO.ReduceItemCharges();
            else destroyObject(item, 0.0f);
        }
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

        NWObject oItem = getPCItemLastEquipped();
        Integer baseItemType = getBaseItemType(oItem);

        if(!Arrays.asList(validItemTypes).contains(baseItemType)) return;

        for(NWItemProperty ip : getItemProperties(oItem))
        {
            if(getItemPropertyType(ip) == ItemProperty.ONHITCASTSPELL)
            {
                if(getItemPropertySubType(ip) == IpConstOnhitCastspell.ONHIT_UNIQUEPOWER)
                {
                    return;
                }
            }
        }

        // No item property found. Add it to the item.
        XP2.IPSafeAddItemProperty(oItem, itemPropertyOnHitCastSpell(IpConstOnhitCastspell.ONHIT_UNIQUEPOWER, 40), 0.0f, AddItemPropertyPolicy.ReplaceExisting, false, false);

        if(baseItemType == BaseItem.TORCH)
        {
            int charges = getItemCharges(oItem) - 1;
            if(charges <= 0)
            {
                destroyObject(oItem, 0.0f);
            }
            else
            {
                setItemCharges(oItem, charges);
            }
        }
    }

    private static void ApplyItemFeatures(NWObject item)
    {
        ItemEntity entity = GetItemEntity(item);

        if(entity == null) return;
        ItemGO itemGO = new ItemGO(item);

        itemGO.setAC(entity.getAc());
        itemGO.setCustomItemType(entity.getItemType().getItemTypeID());
        itemGO.setRecommendedLevel(entity.getRecommendedLevel());
        itemGO.setLoggingBonus(entity.getLoggingBonus());
        itemGO.setMiningBonus(entity.getMiningBonus());
        itemGO.setCastingSpeed(entity.getCastingSpeed());
        itemGO.setCraftBonusMetalworking(entity.getCraftBonusMetalworking());
        itemGO.setCraftBonusArmorsmith(entity.getCraftBonusArmorsmith());
        itemGO.setCraftBonusWeaponsmith(entity.getCraftBonusWeaponsmith());
        itemGO.setCraftBonusCooking(entity.getCraftBonusCooking());
        itemGO.setCraftBonusWoodworking(entity.getCraftBonusWoodworking());
        itemGO.setAssociatedSkillID(entity.getAssociatedSkillID());
        itemGO.setCraftTierLevel(entity.getCraftTierLevel());
        itemGO.setHPBonus(entity.getHpBonus());
        itemGO.setManaBonus(entity.getManaBonus());

        if(entity.getWeight() > 0)
        {
            NWNX_Item.SetWeight(item, entity.getWeight());
        }

        if(entity.getDurabilityPoints() > 0)
        {
            DurabilitySystem.SetItemMaxDurability(item, entity.getDurabilityPoints());
        }
    }
}
