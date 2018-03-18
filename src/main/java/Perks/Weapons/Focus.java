package Perks.Weapons;

import Enumerations.PerkID;
import GameObject.ItemGO;
import GameSystems.PerkSystem;
import NWNX.NWNX_Creature;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.Feat;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

public class Focus implements IPerk {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        return false;
    }

    @Override
    public String CannotCastSpellMessage() {
        return null;
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        return baseManaCost;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        return baseCastingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {

    }

    @Override
    public void OnPurchased(NWObject oPC, int newLevel) {
        ApplyFeatChanges(oPC, null);
    }

    @Override
    public void OnRemoved(NWObject oPC) {
        ApplyFeatChanges(oPC, null);
    }

    @Override
    public void OnItemEquipped(NWObject oPC, NWObject oItem) {
        ApplyFeatChanges(oPC, null);
    }

    @Override
    public void OnItemUnequipped(NWObject oPC, NWObject oItem) {
        ApplyFeatChanges(oPC, oItem);
    }

    private void ApplyFeatChanges(NWObject oPC, NWObject unequippedItem)
    {
        NWObject equipped = unequippedItem == null ? NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC) : unequippedItem;
        ItemGO itemGO = new ItemGO(equipped);

        RemoveAllFeats(oPC);

        // Unarmed check
        NWObject mainHand = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
        NWObject offHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);
        if(unequippedItem != null && unequippedItem.equals(mainHand))
        {
            mainHand = NWObject.INVALID;
        }
        else if(unequippedItem != null && unequippedItem.equals(offHand))
        {
            offHand = NWObject.INVALID;
        }

        if(!NWScript.getIsObjectValid(mainHand) && !NWScript.getIsObjectValid(offHand))
        {
            int martialArtsLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.WeaponFocusMartialArts);
            if(martialArtsLevel >= 1)
            {
                NWNX_Creature.AddFeat(oPC, Feat.WEAPON_FOCUS_UNARMED_STRIKE);
            }
            if(martialArtsLevel >= 2)
            {
                NWNX_Creature.AddFeat(oPC, Feat.WEAPON_SPECIALIZATION_UNARMED_STRIKE);
            }

            return;
        }

        if(unequippedItem != null && unequippedItem.equals(equipped)) return;

        // All other weapon types
        int perkID;
        if(itemGO.IsBlade()) perkID = PerkID.WeaponFocusBlades;
        else if(itemGO.IsFinesseBlade()) perkID = PerkID.WeaponFocusFinesseBlades;
        else if(itemGO.IsBlunt()) perkID = PerkID.WeaponFocusBlunts;
        else if(itemGO.IsHeavyBlade()) perkID = PerkID.WeaponFocusHeavyBlades;
        else if(itemGO.IsHeavyBlunt()) perkID = PerkID.WeaponFocusHeavyBlunts;
        else if(itemGO.IsPolearm()) perkID = PerkID.WeaponFocusPolearms;
        else if(itemGO.IsTwinBlade()) perkID = PerkID.WeaponFocusTwinBlades;
        else if(itemGO.IsMartialArtsWeapon()) perkID = PerkID.WeaponFocusMartialArts;
        else if(itemGO.IsBow()) perkID = PerkID.WeaponFocusBows;
        else if(itemGO.IsCrossbow()) perkID = PerkID.WeaponFocusCrossbows;
        else if(itemGO.IsThrowing()) perkID = PerkID.WeaponFocusThrowing;
        else return;

        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, perkID);
        int type = NWScript.getBaseItemType(equipped);
        if(perkLevel >= 1)
        {
            AddFocusFeat(oPC, type);
        }
        if(perkLevel >= 2)
        {
            AddSpecializationFeat(oPC, type);
        }
    }

    private void RemoveAllFeats(NWObject oPC)
    {
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_BASTARD_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_BATTLE_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_CLUB);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_DAGGER);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_DART);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_DIRE_MACE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_DOUBLE_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_DWAXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_GREAT_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_GREAT_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_HALBERD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_HAND_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_HEAVY_CROSSBOW);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_HEAVY_FLAIL);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_KAMA);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_KATANA);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_KUKRI);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_LIGHT_CROSSBOW);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_LIGHT_FLAIL);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_LIGHT_HAMMER);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_LIGHT_MACE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_LONGBOW);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_LONG_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_MORNING_STAR);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_RAPIER);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_SCIMITAR);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_SCYTHE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_SHORTBOW);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_SHORT_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_SHURIKEN);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_SICKLE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_SLING);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_SPEAR);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_STAFF);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_THROWING_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_TRIDENT);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_TWO_BLADED_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_UNARMED_STRIKE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_WAR_HAMMER);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_FOCUS_WHIP);


        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_BASTARD_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_BATTLE_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_CLUB);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_DAGGER);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_DART);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_DIRE_MACE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_DOUBLE_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_DWAXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_GREAT_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_GREAT_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_HALBERD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_HAND_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_HEAVY_CROSSBOW);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_HEAVY_FLAIL);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_KAMA);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_KATANA);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_KUKRI);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_LIGHT_CROSSBOW);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_LIGHT_FLAIL);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_LIGHT_HAMMER);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_LIGHT_MACE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_LONGBOW);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_LONG_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_MORNING_STAR);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_RAPIER);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_SCIMITAR);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_SCYTHE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_SHORTBOW);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_SHORT_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_SHURIKEN);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_SICKLE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_SLING);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_SPEAR);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_STAFF);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_THROWING_AXE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_TRIDENT);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_TWO_BLADED_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_UNARMED_STRIKE);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_WAR_HAMMER);
        NWNX_Creature.RemoveFeat(oPC, Feat.WEAPON_SPECIALIZATION_WHIP);
    }

    private void AddFocusFeat(NWObject oPC, int type)
    {
        int feat;

        switch (type)
        {
            case(BaseItem.BASTARDSWORD): feat = Feat.WEAPON_FOCUS_BASTARD_SWORD; break;
            case(BaseItem.BATTLEAXE): feat = Feat.WEAPON_FOCUS_BATTLE_AXE; break;
            case(BaseItem.CLUB): feat = Feat.WEAPON_FOCUS_CLUB; break;
            case(BaseItem.DAGGER): feat = Feat.WEAPON_FOCUS_DAGGER; break;
            case(BaseItem.DART): feat = Feat.WEAPON_FOCUS_DART; break;
            case(BaseItem.DIREMACE): feat = Feat.WEAPON_FOCUS_DIRE_MACE; break;
            case(BaseItem.DOUBLEAXE): feat = Feat.WEAPON_FOCUS_DOUBLE_AXE; break;
            case(BaseItem.DWARVENWARAXE): feat = Feat.WEAPON_FOCUS_DWAXE; break;
            case(BaseItem.GREATAXE): feat = Feat.WEAPON_FOCUS_GREAT_AXE; break;
            case(BaseItem.GREATSWORD): feat = Feat.WEAPON_FOCUS_GREAT_SWORD; break;
            case(BaseItem.HALBERD): feat = Feat.WEAPON_FOCUS_HALBERD; break;
            case(BaseItem.HANDAXE): feat = Feat.WEAPON_FOCUS_HAND_AXE; break;
            case(BaseItem.HEAVYCROSSBOW): feat = Feat.WEAPON_FOCUS_HEAVY_CROSSBOW; break;
            case(BaseItem.HEAVYFLAIL): feat = Feat.WEAPON_FOCUS_HEAVY_FLAIL; break;
            case(BaseItem.KAMA): feat = Feat.WEAPON_FOCUS_KAMA; break;
            case(BaseItem.KATANA): feat = Feat.WEAPON_FOCUS_KATANA; break;
            case(BaseItem.KUKRI): feat = Feat.WEAPON_FOCUS_KUKRI; break;
            case(BaseItem.LIGHTCROSSBOW): feat = Feat.WEAPON_FOCUS_LIGHT_CROSSBOW; break;
            case(BaseItem.LIGHTFLAIL): feat = Feat.WEAPON_FOCUS_LIGHT_FLAIL; break;
            case(BaseItem.LIGHTHAMMER): feat = Feat.WEAPON_FOCUS_LIGHT_HAMMER; break;
            case(BaseItem.LIGHTMACE): feat = Feat.WEAPON_FOCUS_LIGHT_MACE; break;
            case(BaseItem.LONGBOW): feat = Feat.WEAPON_FOCUS_LONGBOW; break;
            case(BaseItem.LONGSWORD): feat = Feat.WEAPON_FOCUS_LONG_SWORD; break;
            case(BaseItem.MORNINGSTAR): feat = Feat.WEAPON_FOCUS_MORNING_STAR; break;
            case(BaseItem.RAPIER): feat = Feat.WEAPON_FOCUS_RAPIER; break;
            case(BaseItem.SCIMITAR): feat = Feat.WEAPON_FOCUS_SCIMITAR; break;
            case(BaseItem.SCYTHE): feat = Feat.WEAPON_FOCUS_SCYTHE; break;
            case(BaseItem.SHORTBOW): feat = Feat.WEAPON_FOCUS_SHORTBOW; break;
            case(BaseItem.SHORTSWORD): feat = Feat.WEAPON_FOCUS_SHORT_SWORD; break;
            case(BaseItem.SHURIKEN): feat = Feat.WEAPON_FOCUS_SHURIKEN; break;
            case(BaseItem.SICKLE): feat = Feat.WEAPON_FOCUS_SICKLE; break;
            case(BaseItem.SLING): feat = Feat.WEAPON_FOCUS_SLING; break;
            case(BaseItem.SHORTSPEAR): feat = Feat.WEAPON_FOCUS_SPEAR; break;
            case(BaseItem.QUARTERSTAFF): feat = Feat.WEAPON_FOCUS_STAFF; break;
            case(BaseItem.THROWINGAXE): feat = Feat.WEAPON_FOCUS_THROWING_AXE; break;
            case(BaseItem.TRIDENT): feat = Feat.WEAPON_FOCUS_TRIDENT; break;
            case(BaseItem.TWOBLADEDSWORD): feat = Feat.WEAPON_FOCUS_TWO_BLADED_SWORD; break;
            case(BaseItem.INVALID): feat = Feat.WEAPON_FOCUS_UNARMED_STRIKE; break;
            case(BaseItem.WARHAMMER): feat = Feat.WEAPON_FOCUS_WAR_HAMMER; break;
            case(BaseItem.WHIP): feat = Feat.WEAPON_FOCUS_WHIP; break;
            default: return;
        }

        NWNX_Creature.AddFeat(oPC, feat);
    }


    private void AddSpecializationFeat(NWObject oPC, int type)
    {
        int feat;

        switch (type)
        {
            case(BaseItem.BASTARDSWORD): feat = Feat.WEAPON_SPECIALIZATION_BASTARD_SWORD; break;
            case(BaseItem.BATTLEAXE): feat = Feat.WEAPON_SPECIALIZATION_BATTLE_AXE; break;
            case(BaseItem.CLUB): feat = Feat.WEAPON_SPECIALIZATION_CLUB; break;
            case(BaseItem.DAGGER): feat = Feat.WEAPON_SPECIALIZATION_DAGGER; break;
            case(BaseItem.DART): feat = Feat.WEAPON_SPECIALIZATION_DART; break;
            case(BaseItem.DIREMACE): feat = Feat.WEAPON_SPECIALIZATION_DIRE_MACE; break;
            case(BaseItem.DOUBLEAXE): feat = Feat.WEAPON_SPECIALIZATION_DOUBLE_AXE; break;
            case(BaseItem.DWARVENWARAXE): feat = Feat.WEAPON_SPECIALIZATION_DWAXE; break;
            case(BaseItem.GREATAXE): feat = Feat.WEAPON_SPECIALIZATION_GREAT_AXE; break;
            case(BaseItem.GREATSWORD): feat = Feat.WEAPON_SPECIALIZATION_GREAT_SWORD; break;
            case(BaseItem.HALBERD): feat = Feat.WEAPON_SPECIALIZATION_HALBERD; break;
            case(BaseItem.HANDAXE): feat = Feat.WEAPON_SPECIALIZATION_HAND_AXE; break;
            case(BaseItem.HEAVYCROSSBOW): feat = Feat.WEAPON_SPECIALIZATION_HEAVY_CROSSBOW; break;
            case(BaseItem.HEAVYFLAIL): feat = Feat.WEAPON_SPECIALIZATION_HEAVY_FLAIL; break;
            case(BaseItem.KAMA): feat = Feat.WEAPON_SPECIALIZATION_KAMA; break;
            case(BaseItem.KATANA): feat = Feat.WEAPON_SPECIALIZATION_KATANA; break;
            case(BaseItem.KUKRI): feat = Feat.WEAPON_SPECIALIZATION_KUKRI; break;
            case(BaseItem.LIGHTCROSSBOW): feat = Feat.WEAPON_SPECIALIZATION_LIGHT_CROSSBOW; break;
            case(BaseItem.LIGHTFLAIL): feat = Feat.WEAPON_SPECIALIZATION_LIGHT_FLAIL; break;
            case(BaseItem.LIGHTHAMMER): feat = Feat.WEAPON_SPECIALIZATION_LIGHT_HAMMER; break;
            case(BaseItem.LIGHTMACE): feat = Feat.WEAPON_SPECIALIZATION_LIGHT_MACE; break;
            case(BaseItem.LONGBOW): feat = Feat.WEAPON_SPECIALIZATION_LONGBOW; break;
            case(BaseItem.LONGSWORD): feat = Feat.WEAPON_SPECIALIZATION_LONG_SWORD; break;
            case(BaseItem.MORNINGSTAR): feat = Feat.WEAPON_SPECIALIZATION_MORNING_STAR; break;
            case(BaseItem.RAPIER): feat = Feat.WEAPON_SPECIALIZATION_RAPIER; break;
            case(BaseItem.SCIMITAR): feat = Feat.WEAPON_SPECIALIZATION_SCIMITAR; break;
            case(BaseItem.SCYTHE): feat = Feat.WEAPON_SPECIALIZATION_SCYTHE; break;
            case(BaseItem.SHORTBOW): feat = Feat.WEAPON_SPECIALIZATION_SHORTBOW; break;
            case(BaseItem.SHORTSWORD): feat = Feat.WEAPON_SPECIALIZATION_SHORT_SWORD; break;
            case(BaseItem.SHURIKEN): feat = Feat.WEAPON_SPECIALIZATION_SHURIKEN; break;
            case(BaseItem.SICKLE): feat = Feat.WEAPON_SPECIALIZATION_SICKLE; break;
            case(BaseItem.SLING): feat = Feat.WEAPON_SPECIALIZATION_SLING; break;
            case(BaseItem.SHORTSPEAR): feat = Feat.WEAPON_SPECIALIZATION_SPEAR; break;
            case(BaseItem.QUARTERSTAFF): feat = Feat.WEAPON_SPECIALIZATION_STAFF; break;
            case(BaseItem.THROWINGAXE): feat = Feat.WEAPON_SPECIALIZATION_THROWING_AXE; break;
            case(BaseItem.TRIDENT): feat = Feat.WEAPON_SPECIALIZATION_TRIDENT; break;
            case(BaseItem.TWOBLADEDSWORD): feat = Feat.WEAPON_SPECIALIZATION_TWO_BLADED_SWORD; break;
            case(BaseItem.INVALID): feat = Feat.WEAPON_SPECIALIZATION_UNARMED_STRIKE; break;
            case(BaseItem.WARHAMMER): feat = Feat.WEAPON_SPECIALIZATION_WAR_HAMMER; break;
            case(BaseItem.WHIP): feat = Feat.WEAPON_SPECIALIZATION_WHIP; break;
            default: return;
        }

        NWNX_Creature.AddFeat(oPC, feat);
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
