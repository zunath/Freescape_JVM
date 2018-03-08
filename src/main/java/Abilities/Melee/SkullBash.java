package Abilities.Melee;

import Abilities.IAbility;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.Duration;

import java.util.concurrent.ThreadLocalRandom;

// Deals extra bludgeoning damage to a target on the next blunt attack.
// Also inflicts a temporary knockdown effect.
// A blunt weapon must be equipped.
// The knockdown effect lasts for between 1 and 4 seconds.
// Deals between 3 and 8 bludgeoning damage.
public class SkullBash implements IAbility {
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

        if(!ItemHelper.IsBlunt(oItem))
        {
            NWScript.sendMessageToPC(oPC, "Skull Bash can only be used with blunt weapons.");
            return;
        }

        int damage = ThreadLocalRandom.current().nextInt(3, 8);
        NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectDamage(damage, DamageType.BLUDGEONING, DamagePower.NORMAL), oTarget, 0.0f);

        float duration = (float)ThreadLocalRandom.current().nextDouble(1.0, 4.0);
        NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, NWScript.effectKnockdown(), oTarget, duration);
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
