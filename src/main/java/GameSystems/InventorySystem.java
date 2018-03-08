package GameSystems;

import Common.Constants;
import Entities.PlayerEntity;
import Entities.PlayerProgressionSkillEntity;
import Enumerations.AbilityType;
import Enumerations.CustomItemProperty;
import Enumerations.ProfessionType;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Data.Repository.PlayerProgressionSkillsRepository;
import Data.Repository.PlayerRepository;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.EffectType;

import java.util.Objects;

public class InventorySystem {

    private static final int BaseInventoryLimit = 20;


    public static void OnModuleAcquireItem()
    {
        NWObject oPC = NWScript.getModuleItemAcquiredBy();
        NWObject oItem = NWScript.getModuleItemAcquired();
        RunItemLimitCheck(oPC, oItem);
    }

    public static void OnModuleEquipItem()
    {
        NWObject oPC = NWScript.getPCItemLastEquippedBy();
        NWObject oItem = NWScript.getPCItemLastEquipped();
        RunItemLimitCheck(oPC, oItem);
    }

    public static void OnModuleUnAcquireItem()
    {
        NWObject oPC = NWScript.getModuleItemLostBy();
        NWObject oItem = NWScript.getModuleItemLost();
        RunItemLimitCheck(oPC, oItem);
    }

    public static void OnModuleUnEquipItem()
    {
        final NWObject oPC = NWScript.getPCItemLastUnequippedBy();
        final NWObject oItem = NWScript.getPCItemLastUnequipped();
        Scheduler.delay(oPC, 100, () -> RunItemLimitCheck(oPC, oItem));
    }

    private static int GetItemCount(NWObject oPC)
    {
        int count = 0;
        for(NWObject item: NWScript.getItemsInInventory(oPC))
        {
            if(!IsItemExempt(item))
            {
                count++;
            }
        }
        return count;
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean IsItemExempt(NWObject oItem)
    {
        String sResref = NWScript.getResRef(oItem);
        String sTag = NWScript.getTag(oItem);
        String sName = NWScript.getName(oItem, false);

        // Specific items
        if(Objects.equals(sResref, Constants.PCDatabaseTag) ||  // Database item
                Objects.equals(sResref, "dmfi_dicebag") ||  // DMFI Dicebag
                Objects.equals(sResref, "dmfi_pc_emote") ||  // DMFI PC Emote Wand
                Objects.equals(sResref, "fky_chat_target") ||  // SimTools Command Targeter
                Objects.equals(sResref, "firearm_magazine") ||  // Combat system firearm magazine
                Objects.equals(sResref, "e_gun_mag") ||  // Combat system enhanced firearm magazine
                Objects.equals(sResref, "i_gun_mag") ||  // Combat system incendiary firearm magazine
                Objects.equals(sResref, "nw_it_gold001") ||  // Gold
                Objects.equals(sResref, "rhs_furn_tool") ||  // Furniture Tool
                Objects.equals(sResref, "") ||  // Item doesn't have a resref
                Objects.equals(sResref, "cft_choose_bp") || // Craft system "Choose Blueprint"
                Objects.equals(sResref, "cft_craft_item") || // Craft system "Craft Item"
                Objects.equals(sResref, "infection_crud") || // Infection system "Infection Item"
                NWScript.getLocalInt(oItem, "ZEP_CR_TEMPITEM") != 0 ||  // CEP Crafting GameSystems - Prevents a bug when PC tries to craft armor appearance on full inventory
                Objects.equals(sName, "PC Properties") ||               // Patch 1.69 PC properties skin. Can't get the tag of this for whatever reason so I use its name.
                Objects.equals(NWScript.getStringLeft(sTag, 8), "KEYITEM_") || // Key items
                NWScript.getItemCursedFlag(oItem))
            return true;



        return false;
    }

    private static int GetPlayerInventoryLimit(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        String uuid = pcGO.getUUID();

        PlayerEntity entity = new PlayerRepository().GetByPlayerID(uuid);
        PlayerProgressionSkillEntity skillEntity = new PlayerProgressionSkillsRepository().GetByPlayerIDAndSkillID(uuid, ProgressionSystem.SkillType_INVENTORY_SPACE);
        int slots = BaseInventoryLimit + (skillEntity == null ? 0 : skillEntity.getUpgradeLevel());

        if(entity != null)
        {
            slots += entity.getInventorySpaceBonus();
            // Merchant Profession grants +4 item slots.
            slots += entity.getProfessionID() == ProfessionType.Merchant ? 8 : 0;
        }

        int equipBonusSlots = 0;
        for(int invSlot = 0; invSlot < Constants.NumberOfInventorySlots; invSlot++)
        {
            NWObject oItem = NWScript.getItemInSlot(invSlot, oPC);
            if(!oItem.equals(NWObject.INVALID))
            {
                int itemBonusCount = 0;
                for(NWItemProperty ip : NWScript.getItemProperties(oItem))
                {
                    if(NWScript.getItemPropertyType(ip) == CustomItemProperty.InventorySpaceBonus)
                    {
                        int count = NWScript.getItemPropertyCostTableValue(ip);
                        if(count > itemBonusCount)
                        {
                            itemBonusCount = count;
                        }
                    }
                }

                equipBonusSlots += itemBonusCount;
            }
        }

        // Strong Back ability grants +10 item slots.
        int abilityBonusSlots = MagicSystem.IsAbilityEquipped(oPC, AbilityType.StrongBack) ? 10 : 0;

        // +1 inventory every other STR starting at 1. (I.E: 1, 3, 5, 7, 9, 10, etc)
        int strength = NWScript.getAbilityScore(oPC, Ability.STRENGTH, false) - 10;
        int strBonusSlots = 0;
        for(int index = 1; index <= strength; index+=2)
        {
            strBonusSlots += 1;
        }

        return slots + equipBonusSlots + abilityBonusSlots + strBonusSlots;
    }

    private static void RunItemLimitCheck(NWObject oPC, NWObject oItem)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || IsItemExempt(oItem)) return;
        RunItemLimitCheck(oPC);
    }

    public static void RunItemLimitCheck(NWObject oPC)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        int numberOfItems = GetItemCount(oPC);
        int limit = GetPlayerInventoryLimit(oPC);

        NWEffect[] effects = NWScript.getEffects(oPC);
        for(NWEffect effect : effects)
        {
            if(NWScript.getEffectType(effect) == EffectType.MOVEMENT_SPEED_DECREASE ||
                    NWScript.getEffectType(effect) == EffectType.CUTSCENEIMMOBILIZE)
            {
                NWScript.removeEffect(oPC, effect);
            }
        }

        if(numberOfItems > limit)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Gray() + "Inventory: " + ColorToken.End() +
                    ColorToken.Red() + numberOfItems + ColorToken.End() +
                    ColorToken.Gray() + " / " + limit + ColorToken.End() +
                    ColorToken.Red() + " (ENCUMBERED)" + ColorToken.End());

            int speedDecreaseAmount = (numberOfItems - limit) * 15;

            if(speedDecreaseAmount > 99)
            {
                NWScript.applyEffectToObject(DurationType.PERMANENT, NWScript.effectCutsceneImmobilize(), oPC, 0.0f);
            }
            else
            {
                NWScript.applyEffectToObject(DurationType.PERMANENT, NWScript.effectMovementSpeedDecrease(speedDecreaseAmount), oPC, 0.0f);
            }
        }
        else
        {
            String color = numberOfItems > limit ? ColorToken.Red() : ColorToken.Gray();
            NWScript.sendMessageToPC(oPC, ColorToken.Gray() + "Inventory: " + numberOfItems + " / " + color + limit + ColorToken.End());

        }
    }


}

