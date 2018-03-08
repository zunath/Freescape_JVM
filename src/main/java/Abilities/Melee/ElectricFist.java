package Abilities.Melee;

import Abilities.IAbility;
import Enumerations.CustomEffectType;
import GameSystems.CustomEffectSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.Duration;

import java.util.concurrent.ThreadLocalRandom;

// Deals extra electrical damage to a target on the next unarmed strike attack.
// Also inflicts a temporary shock effect on the target.
// Gauntlets must be equipped and no weapon must be equipped.
// The shock effect lasts for between 3 and 5 ticks (6 seconds per tick)
// Deals between 8 and 15 electrical damage.
public class ElectricFist implements IAbility {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        return true;
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
        NWObject oItem = NWScript.getSpellCastItem();
        int itemType = NWScript.getBaseItemType(oItem);

        if(itemType != BaseItem.GLOVES)
        {
            NWScript.sendMessageToPC(oPC, "Electric Fist can only be used with gauntlets.");
            return;
        }

        int ticks = ThreadLocalRandom.current().nextInt(3, 5);
        CustomEffectSystem.ApplyCustomEffect(oPC, oTarget, CustomEffectType.Shock, ticks);

        int damage = ThreadLocalRandom.current().nextInt(8, 15);
        NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectDamage(damage, DamageType.ELECTRICAL, DamagePower.NORMAL), oTarget, 0.0f);
    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
