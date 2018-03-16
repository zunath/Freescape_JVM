package Perks.Shields;

import Enumerations.PerkID;
import Helper.ColorToken;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.AttackBonus;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

import static GameSystems.PerkSystem.GetPCPerkLevel;

public class DefensiveManeuver implements IPerk {
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

        int perkLevel = GetPCPerkLevel(oPC, PerkID.DefensiveManeuver);
        float length;
        int ab;
        int chance;

        switch (perkLevel)
        {
            case 1:
                length = 12.0f;
                ab = 1;
                chance = 10;
                break;
            case 2:
                length = 12.0f;
                ab = 1;
                chance = 20;
                break;
            case 3:
                length = 12.0f;
                ab = 2;
                chance = 20;
                break;
            case 4:
                length = 12.0f;
                ab = 2;
                chance = 30;
                break;
            case 5:
                length = 12.0f;
                ab = 3;
                chance = 30;
                break;
            default:
                return;
        }

        int luck = GetPCPerkLevel(oPC, PerkID.Lucky);
        chance += luck;

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= chance)
        {
            NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectAttackIncrease(ab, AttackBonus.MISC), oPC, length);
            NWScript.sendMessageToPC(oPC, ColorToken.Combat() + "You perform a defensive maneuver." + ColorToken.End());
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
