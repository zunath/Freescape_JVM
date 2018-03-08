package Abilities.Melee;

import Abilities.IAbility;
import Enumerations.CustomEffectType;
import GameSystems.CustomEffectSystem;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.Duration;

import java.util.concurrent.ThreadLocalRandom;

// Deals extra fire damage to a target on the next blade attack.
// Also inflicts a temporary burn effect on the target.
// A blade must be equipped.
// The burn effect lasts for between 3 and 5 ticks (6 seconds per tick)
// Deals between 6 and 12 fire damage.
public class FlameBlade implements IAbility {
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

        if(!ItemHelper.IsBlade(oItem))
        {
            NWScript.sendMessageToPC(oPC, "Flame Blade can only be used with blade weapons.");
            return;
        }

        int ticks = ThreadLocalRandom.current().nextInt(1, 3);
        CustomEffectSystem.ApplyCustomEffect(oPC, oTarget, CustomEffectType.Burn, ticks);

        int damage = ThreadLocalRandom.current().nextInt(6, 12);
        NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectDamage(damage, DamageType.FIRE, DamagePower.NORMAL), oTarget, 0.0f);

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
