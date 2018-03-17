package Perks.Weapons;

import Enumerations.CustomItemType;
import Enumerations.PerkID;
import GameObject.ItemGO;
import GameSystems.PerkSystem;
import NWNX.NWNX_Creature;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.Feat;
import org.nwnx.nwnx2.jvm.constants.Inventory;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

public class ImprovedCritical implements IPerk {
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
            if(PerkSystem.GetPCPerkLevel(oPC, PerkID.ImprovedCriticalMartialArts) > 0)
            {
                NWNX_Creature.AddFeat(oPC, Feat.IMPROVED_CRITICAL_UNARMED_STRIKE);
            }
            return;
        }

        if(unequippedItem != null && unequippedItem.equals(equipped)) return;

        // All other weapon types
        int perkID;
        if(itemGO.IsBlade()) perkID = PerkID.ImprovedCriticalBlades;
        else if(itemGO.IsFinesseBlade()) perkID = PerkID.ImprovedCriticalFinesseBlades;
        else if(itemGO.IsBlunt()) perkID = PerkID.ImprovedCriticalBlunts;
        else if(itemGO.IsHeavyBlade()) perkID = PerkID.ImprovedCriticalHeavyBlades;
        else if(itemGO.IsHeavyBlunt()) perkID = PerkID.ImprovedCriticalHeavyBlunts;
        else if(itemGO.IsPolearm()) perkID = PerkID.ImprovedCriticalPolearms;
        else if(itemGO.IsTwinBlade()) perkID = PerkID.ImprovedCriticalTwinBlades;
        else if(itemGO.IsMartialArtsWeapon()) perkID = PerkID.ImprovedCriticalMartialArts;
        else if(itemGO.IsBow()) perkID = PerkID.ImprovedCriticalBows;
        else if(itemGO.IsCrossbow()) perkID = PerkID.ImprovedCriticalCrossbows;
        else if(itemGO.IsThrowing()) perkID = PerkID.ImprovedCriticalThrowing;
        else return;

        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, perkID);
        int type = NWScript.getBaseItemType(equipped);
        if(perkLevel > 0)
        {
            AddCriticalFeat(oPC, type);
        }
    }

    private void RemoveAllFeats(NWObject oPC)
    {
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_BASTARD_SWORD);
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_BATTLE_AXE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_CLUB );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_DAGGER );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_DART );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_DIRE_MACE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_DOUBLE_AXE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_DWAXE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_GREAT_AXE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_GREAT_SWORD );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_HALBERD );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_HAND_AXE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_HEAVY_CROSSBOW );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_HEAVY_FLAIL );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_KAMA );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_KATANA );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_KUKRI );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_LIGHT_CROSSBOW );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_LIGHT_FLAIL );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_LIGHT_HAMMER );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_LIGHT_MACE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_LONGBOW );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_LONG_SWORD );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_MORNING_STAR );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_RAPIER );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_SCIMITAR );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_SCYTHE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_SHORTBOW );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_SHORT_SWORD );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_SHURIKEN );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_SICKLE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_SLING );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_SPEAR );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_STAFF );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_THROWING_AXE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_TRIDENT );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_TWO_BLADED_SWORD );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_UNARMED_STRIKE );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_WAR_HAMMER );
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_CRITICAL_WHIP);
    }

    private void AddCriticalFeat(NWObject oPC, int type)
    {
        int feat;

        switch (type)
        {
            case(BaseItem.BASTARDSWORD): feat = Feat.IMPROVED_CRITICAL_BASTARD_SWORD; break;
            case(BaseItem.BATTLEAXE): feat = Feat.IMPROVED_CRITICAL_BATTLE_AXE; break;
            case(BaseItem.CLUB): feat = Feat.IMPROVED_CRITICAL_CLUB; break;
            case(BaseItem.DAGGER): feat = Feat.IMPROVED_CRITICAL_DAGGER; break;
            case(BaseItem.DART): feat = Feat.IMPROVED_CRITICAL_DART; break;
            case(BaseItem.DIREMACE): feat = Feat.IMPROVED_CRITICAL_DIRE_MACE; break;
            case(BaseItem.DOUBLEAXE): feat = Feat.IMPROVED_CRITICAL_DOUBLE_AXE; break;
            case(BaseItem.DWARVENWARAXE): feat = Feat.IMPROVED_CRITICAL_DWAXE; break;
            case(BaseItem.GREATAXE): feat = Feat.IMPROVED_CRITICAL_GREAT_AXE; break;
            case(BaseItem.GREATSWORD): feat = Feat.IMPROVED_CRITICAL_GREAT_SWORD; break;
            case(BaseItem.HALBERD): feat = Feat.IMPROVED_CRITICAL_HALBERD; break;
            case(BaseItem.HANDAXE): feat = Feat.IMPROVED_CRITICAL_HAND_AXE; break;
            case(BaseItem.HEAVYCROSSBOW): feat = Feat.IMPROVED_CRITICAL_HEAVY_CROSSBOW; break;
            case(BaseItem.HEAVYFLAIL): feat = Feat.IMPROVED_CRITICAL_HEAVY_FLAIL; break;
            case(BaseItem.KAMA): feat = Feat.IMPROVED_CRITICAL_KAMA; break;
            case(BaseItem.KATANA): feat = Feat.IMPROVED_CRITICAL_KATANA; break;
            case(BaseItem.KUKRI): feat = Feat.IMPROVED_CRITICAL_KUKRI; break;
            case(BaseItem.LIGHTCROSSBOW): feat = Feat.IMPROVED_CRITICAL_LIGHT_CROSSBOW; break;
            case(BaseItem.LIGHTFLAIL): feat = Feat.IMPROVED_CRITICAL_LIGHT_FLAIL; break;
            case(BaseItem.LIGHTHAMMER): feat = Feat.IMPROVED_CRITICAL_LIGHT_HAMMER; break;
            case(BaseItem.LIGHTMACE): feat = Feat.IMPROVED_CRITICAL_LIGHT_MACE; break;
            case(BaseItem.LONGBOW): feat = Feat.IMPROVED_CRITICAL_LONGBOW; break;
            case(BaseItem.LONGSWORD): feat = Feat.IMPROVED_CRITICAL_LONG_SWORD; break;
            case(BaseItem.MORNINGSTAR): feat = Feat.IMPROVED_CRITICAL_MORNING_STAR; break;
            case(BaseItem.RAPIER): feat = Feat.IMPROVED_CRITICAL_RAPIER; break;
            case(BaseItem.SCIMITAR): feat = Feat.IMPROVED_CRITICAL_SCIMITAR; break;
            case(BaseItem.SCYTHE): feat = Feat.IMPROVED_CRITICAL_SCYTHE; break;
            case(BaseItem.SHORTBOW): feat = Feat.IMPROVED_CRITICAL_SHORTBOW; break;
            case(BaseItem.SHORTSWORD): feat = Feat.IMPROVED_CRITICAL_SHORT_SWORD; break;
            case(BaseItem.SHURIKEN): feat = Feat.IMPROVED_CRITICAL_SHURIKEN; break;
            case(BaseItem.SICKLE): feat = Feat.IMPROVED_CRITICAL_SICKLE; break;
            case(BaseItem.SLING): feat = Feat.IMPROVED_CRITICAL_SLING; break;
            case(BaseItem.SHORTSPEAR): feat = Feat.IMPROVED_CRITICAL_SPEAR; break;
            case(BaseItem.QUARTERSTAFF): feat = Feat.IMPROVED_CRITICAL_STAFF; break;
            case(BaseItem.THROWINGAXE): feat = Feat.IMPROVED_CRITICAL_THROWING_AXE; break;
            case(BaseItem.TRIDENT): feat = Feat.IMPROVED_CRITICAL_TRIDENT; break;
            case(BaseItem.TWOBLADEDSWORD): feat = Feat.IMPROVED_CRITICAL_TWO_BLADED_SWORD; break;
            case(BaseItem.INVALID): feat = Feat.IMPROVED_CRITICAL_UNARMED_STRIKE; break;
            case(BaseItem.WARHAMMER): feat = Feat.IMPROVED_CRITICAL_WAR_HAMMER; break;
            case(BaseItem.WHIP): feat = Feat.IMPROVED_CRITICAL_WHIP; break;
            default: return;
        }

        NWNX_Creature.AddFeat(oPC, feat);
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
