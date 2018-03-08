package Abilities.Passive;

import Abilities.IAbility;
import NWNX.NWNX_Creature;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Feat;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

// Grants Ambidexterity and Two Weapon Fighting feats.
public class DualWield implements IAbility {
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
        NWNX_Creature.AddFeatByLevel(oPC, Feat.AMBIDEXTERITY, 1);
        NWNX_Creature.AddFeatByLevel(oPC, Feat.TWO_WEAPON_FIGHTING, 1);

        final NWObject offHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);

        if(offHand != NWObject.INVALID)
        {
            Scheduler.assign(oPC, () -> {
                NWScript.actionUnequipItem(offHand);
                NWScript.actionEquipItem(offHand, InventorySlot.LEFTHAND);
            });
        }

    }

    @Override
    public void OnUnequip(NWObject oPC) {
        NWNX_Creature.RemoveFeat(oPC, Feat.AMBIDEXTERITY);
        NWNX_Creature.RemoveFeat(oPC, Feat.TWO_WEAPON_FIGHTING);

        final NWObject offHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);

        if(offHand != NWObject.INVALID)
        {
            Scheduler.assign(oPC, () -> {
                NWScript.actionUnequipItem(offHand);
                NWScript.actionEquipItem(offHand, InventorySlot.LEFTHAND);
            });
        }

    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
