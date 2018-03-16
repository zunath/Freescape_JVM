package Perks.OneHanded;

import NWNX.NWNX_Creature;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.Feat;

public class DualWielding implements IPerk {
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
    public void OnPurchased(NWObject oPC, int newLevel) {
        switch (newLevel)
        {
            case 1:
                NWNX_Creature.AddFeat(oPC, Feat.TWO_WEAPON_FIGHTING);
                break;
            case 2:
                NWNX_Creature.AddFeat(oPC, Feat.AMBIDEXTERITY);
                break;
            case 3:
                NWNX_Creature.AddFeat(oPC, Feat.IMPROVED_TWO_WEAPON_FIGHTING);
                break;
        }
    }

    @Override
    public void OnRemoved(NWObject oPC) {
        NWNX_Creature.RemoveFeat(oPC, Feat.TWO_WEAPON_FIGHTING);
        NWNX_Creature.RemoveFeat(oPC, Feat.AMBIDEXTERITY);
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_TWO_WEAPON_FIGHTING);
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
