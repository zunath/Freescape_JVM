package Perks.Shields;

import Enumerations.PerkID;
import GameSystems.PerkSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

public class BlockingRecovery implements IPerk {
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
    public void OnImpact(NWObject oPC, NWObject oItem) {
        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.BlockingRecovery);
        int chance;
        int amount;

        switch (perkLevel)
        {
            case 1:
                chance = 50;
                amount = 1;
                break;
            case 2:
                chance = 50;
                amount = 2;
                break;
            case 3:
                chance = 50;
                amount = 3;
                break;
            case 4:
                chance = 75;
                amount = 3;
                break;
            case 5:
                chance = 75;
                amount = 4;
                break;
            default:
                return;
        }

        int luck = PerkSystem.GetPCPerkLevel(oPC, PerkID.Lucky);
        chance += luck;

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= chance)
        {
            NWEffect heal = NWScript.effectHeal(amount);
            NWScript.applyEffectToObject(DurationType.INSTANT, heal, oPC, 0.0f);
        }
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
