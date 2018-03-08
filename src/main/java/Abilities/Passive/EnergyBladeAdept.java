package Abilities.Passive;

import Abilities.IAbility;
import Enumerations.CustomItemProperty;
import GameObject.ItemGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

// Enables energy blades to be equipped.
public class EnergyBladeAdept implements IAbility {
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

    }

    @Override
    public void OnUnequip(NWObject oPC) {
        final NWObject rightHand = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
        final NWObject leftHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);
        ItemGO rightGO = new ItemGO(rightHand);
        ItemGO leftGO = new ItemGO(leftHand);

        if(rightGO.HasItemProperty(CustomItemProperty.EnergyBlade))
        {
            Scheduler.assign(oPC, () -> {
                NWScript.clearAllActions(false);
                NWScript.actionUnequipItem(rightHand);
            });
        }

        if(leftGO.HasItemProperty(CustomItemProperty.EnergyBlade))
        {
            Scheduler.assign(oPC, () -> {
                NWScript.clearAllActions(false);
                NWScript.actionUnequipItem(leftHand);
            });
        }
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
