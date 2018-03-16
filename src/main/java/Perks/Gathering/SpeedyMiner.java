package Perks.Gathering;

import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;

public class SpeedyMiner implements IPerk {
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
        return 0;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        return 0;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return 0;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {

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
