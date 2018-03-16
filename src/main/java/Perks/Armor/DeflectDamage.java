package Perks.Armor;

import Enumerations.CustomItemType;
import Enumerations.PerkID;
import GameObject.ItemGO;
import GameSystems.PerkSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.*;

public class DeflectDamage implements IPerk {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        NWObject armor = NWScript.getItemInSlot(InventorySlot.CHEST, oPC);
        ItemGO itemGO = new ItemGO(armor);
        return itemGO.getCustomItemType() == CustomItemType.HeavyArmor;
    }

    @Override
    public String CannotCastSpellMessage() {
        return "You must be equipped with heavy armor to use that combat ability.";
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
        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.Evasiveness);
        int damageBase;
        float length = 12.0f;
        int randomDamage;

        switch(perkLevel)
        {
            case 1:
                damageBase = 1;
                randomDamage = 6; // 6 = DAMAGE_BONUS_1d4 constant
                break;
            case 2:
                damageBase = 1;
                randomDamage = 8; // 8 = DAMAGE_BONUS_1d8 constant
                break;
            case 3:
                damageBase = 2;
                randomDamage = 10; // 10 = DAMAGE_BONUS_2d6 constant
                break;
            case 4:
                damageBase = 2;
                randomDamage = 11; // 11 = DAMAGE_BONUS_2d8 constant
                break;
            case 5:
                damageBase = 3;
                randomDamage = 15; // 15 = DAMAGE_BONUS_2d12 constant
                break;
            default:
                return;
        }

        NWEffect effect = NWScript.effectDamageShield(damageBase, randomDamage, DamageType.MAGICAL);
        NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oPC, length);

        effect = NWScript.effectVisualEffect(VfxDurAura.ORANGE, false);
        NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oPC, length);

        effect = NWScript.effectVisualEffect(VfxImp.AC_BONUS, false);
        NWScript.applyEffectToObject(DurationType.INSTANT, effect, oPC, 0.0f);
    }

    @Override
    public void OnPurchased(NWObject oPC, int newLevel) {

    }

    @Override
    public void OnRemoved(NWObject oPC) {

    }

    @Override
    public void OnItemEquipped(NWObject oPC, NWObject oItem) {

    }

    @Override
    public void OnItemUnequipped(NWObject oPC, NWObject oItem) {

    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
