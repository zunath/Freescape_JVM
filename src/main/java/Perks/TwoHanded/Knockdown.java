package Perks.TwoHanded;

import Enumerations.PerkID;
import GameSystems.PerkSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.applyEffectToObject;
import static org.nwnx.nwnx2.jvm.NWScript.effectKnockdown;

public class Knockdown implements IPerk {
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
        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.Knockdown);
        int chance;
        float length;

        switch (perkLevel)
        {
            case 1:
                chance = 10;
                length = 3.0f;
                break;
            case 2:
                chance = 20;
                length = 3.0f;
                break;
            case 3:
                chance = 20;
                length = 6.0f;
                break;
            case 4:
                chance = 30;
                length = 6.0f;
                break;
            case 5:
                chance = 40;
                length = 6.0f;
                break;
            case 6:
                chance = 50;
                length = 6.0f;
                break;
            default: return;
        }

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= chance)
        {
            applyEffectToObject(DurationType.TEMPORARY, effectKnockdown(), oTarget, length);
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
