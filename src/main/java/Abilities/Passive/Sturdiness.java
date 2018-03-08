package Abilities.Passive;

import Abilities.IAbility;
import NWNX.NWNX_Creature;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;

// Increases max hit points by 10 points.
public class Sturdiness implements IAbility {
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
    public void OnEquip(NWObject oPC) {
        NWNX_Creature.SetMaxHitPointsByLevel(oPC, 1, NWScript.getMaxHitPoints(oPC) + 10);
    }

    @Override
    public void OnUnequip(NWObject oPC) {

        NWNX_Creature.SetMaxHitPointsByLevel(oPC, 1, NWScript.getMaxHitPoints(oPC) - 10);

        if(NWScript.getCurrentHitPoints(oPC) > NWScript.getMaxHitPoints(oPC))
        {
            NWEffect damageEffect = NWScript.effectDamage(NWScript.getCurrentHitPoints(oPC) - NWScript.getMaxHitPoints(oPC), DamageType.MAGICAL, DamagePower.NORMAL);
            NWScript.applyEffectToObject(DurationType.INSTANT, damageEffect, oPC, 0.0f);
        }
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
