package Abilities.Ranged;

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

// Deals extra piercing damage to a target on the next throwing weapon attack.
// Also inflicts a temporary bleeding effect on the target.
// A throwing weapon must be equipped.
// The bleeding effect lasts for between 3 and 8 ticks (6 seconds per tick)
// Deals between 4 and 6 piercing damage.
public class PiercingToss implements IAbility {
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

        if(!ItemHelper.IsThrowing(oItem))
        {
            NWScript.sendMessageToPC(oPC, "Piercing Toss can only be used with throwing weapons.");
            return;
        }

        int ticks = ThreadLocalRandom.current().nextInt(3, 8);
        CustomEffectSystem.ApplyCustomEffect(oPC, oTarget, CustomEffectType.PiercingTossBleeding, ticks);

        int damage = ThreadLocalRandom.current().nextInt(4, 6);
        NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectDamage(damage, DamageType.PIERCING, DamagePower.NORMAL), oTarget, 0.0f);

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
