package Abilities.Passive;

import Abilities.IAbility;
import GameSystems.InventorySystem;
import org.nwnx.nwnx2.jvm.NWObject;

// Grants +10 item slots.
public class StrongBack implements IAbility {
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
        InventorySystem.RunItemLimitCheck(oPC);
    }

    @Override
    public void OnUnequip(NWObject oPC) {
        InventorySystem.RunItemLimitCheck(oPC);
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
