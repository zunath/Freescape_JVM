package Abilities.Passive;

import Abilities.IAbility;
import NWNX.NWNX_Creature;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;

// Increases constitution by 2 points.
public class Tough implements IAbility {
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
        NWNX_Creature.NWNX_Creature_ModifyRawAbilityScore(oPC, Ability.CONSTITUTION, 2);
    }

    @Override
    public void OnUnequip(NWObject oPC)
    {
        NWNX_Creature.NWNX_Creature_ModifyRawAbilityScore(oPC, Ability.CONSTITUTION, -2);
    }
    @Override
    public boolean IsHostile() {
        return false;
    }
}
