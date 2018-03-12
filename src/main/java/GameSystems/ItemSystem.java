package GameSystems;

import Bioware.AddItemPropertyPolicy;
import Bioware.XP2;
import Data.Repository.ItemRepository;
import Entities.ItemEntity;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ItemHelper;
import Helper.ScriptHelper;
import Item.IActionItem;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.*;
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
        if(itemGO.getLoggingBonus() > 0)
        {
            description += ColorToken.Orange() + "Logging Bonus: " + ColorToken.End() + itemGO.getLoggingBonus() + "\n";
        }
        if(itemGO.getMiningBonus() > 0)
        {
            description += ColorToken.Orange() + "Mining Bonus: " + ColorToken.End() + itemGO.getMiningBonus() + "\n";
        }

        return existingDescription + "\n" + description;
    }

    public static void OnModuleActivatedItem()
    {
        NWObject user = NWScript.getItemActivator();
        if(!NWScript.getIsPC(user) || NWScript.getIsDM(user) || NWScript.getIsDMPossessed(user)) return;

        PlayerGO pcGO = new PlayerGO(user);
        NWObject target = NWScript.getItemActivatedTarget();
        NWObject item = NWScript.getItemActivated();
        String actionScript = NWScript.getLocalString(item, "JAVA_ACTION_SCRIPT");
        if(actionScript.equals("")) return;

        IActionItem actionItem = (IActionItem) ScriptHelper.GetClassByName("Item." + actionScript);
        if(actionItem == null) return;

        if(pcGO.isBusy())
        {
            NWScript.sendMessageToPC(user, "You are busy.");
            return;
        }

        String invalidTargetMessage = actionItem.IsValidTarget(user, item, target);
        if(invalidTargetMessage != null && !invalidTargetMessage.equals(""))
        {
            NWScript.sendMessageToPC(user, invalidTargetMessage);
            return;
        }

        if(actionItem.MaxDistance() > 0.0f)
        {
            if(NWScript.getDistanceBetween(user, target) > actionItem.MaxDistance() ||
                    !NWScript.getResRef(NWScript.getArea(user)).equals(NWScript.getResRef(NWScript.getArea(target))))
            {
                NWScript.sendMessageToPC(user, "Your target is too far away.");
                return;
            }
        }

        Object customData = actionItem.StartUseItem(user, item, target);
        float delay = actionItem.Seconds(user, item, target);
        int animationID = actionItem.AnimationID();
        boolean faceTarget = actionItem.FaceTarget();
        NWVector userPosition = NWScript.getPosition(user);

        Scheduler.assign(user, () -> {
            pcGO.setIsBusy(true);
            if(faceTarget)
                NWScript.setFacingPoint(NWScript.getPosition(target));
            if(animationID > 0)
                NWScript.actionPlayAnimation(animationID, 1.0f, delay);
        });

        NWNX_Player.StartGuiTimingBar(user, delay, "");
        Scheduler.delay(user, (int)(delay * 1000), () -> FinishActionItem(actionItem, user, item, target, userPosition, customData));
    }

    private static void FinishActionItem(IActionItem actionItem, NWObject user, NWObject item, NWObject target, NWVector userStartPosition, Object customData)
    {
        PlayerGO pcGO = new PlayerGO(user);
        pcGO.setIsBusy(false);

        NWVector userPosition = NWScript.getPosition(user);
        if(!userPosition.equals(userStartPosition))
        {
            NWScript.sendMessageToPC(user, "You move and interrupt your action.");
            return;
        }

        if(actionItem.MaxDistance() > 0.0f)
        {
            if(NWScript.getDistanceBetween(user, target) > actionItem.MaxDistance() ||
                    !NWScript.getResRef(NWScript.getArea(user)).equals(NWScript.getResRef(NWScript.getArea(target))))
            {
                NWScript.sendMessageToPC(user, "Your target is too far away.");
                return;
            }
        }

        if(!NWScript.getIsObjectValid(target))
        {
            NWScript.sendMessageToPC(user, "Unable to locate target.");
            return;
        }

        String invalidTargetMessage = actionItem.IsValidTarget(user, item, target);
        if(invalidTargetMessage != null && !invalidTargetMessage.equals(""))
        {
            NWScript.sendMessageToPC(user, invalidTargetMessage);
            return;
        }

        actionItem.ApplyEffects(user, item, target, customData);

        if(NWScript.getItemCharges(item) > 0) ItemHelper.ReduceItemCharges(item);
        else NWScript.destroyObject(item, 0.0f);
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
        itemGO.setLoggingBonus(entity.getLoggingBonus());
        itemGO.setMiningBonus(entity.getMiningBonus());
    }
}
