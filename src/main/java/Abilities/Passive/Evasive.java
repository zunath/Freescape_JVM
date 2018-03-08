package Abilities.Passive;

import Abilities.IAbility;
import NWNX.NWNX_Creature;
import org.nwnx.nwnx2.jvm.NWObject;

// Increases natural AC bonus by 2 points.
public class Evasive implements IAbility {
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
        NWNX_Creature.SetBaseAC(oPC, NWNX_Creature.GetBaseAC(oPC) + 2);
    }

    @Override
    public void OnUnequip(NWObject oPC) {
        NWNX_Creature.SetBaseAC(oPC, NWNX_Creature.GetBaseAC(oPC) - 2);
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
