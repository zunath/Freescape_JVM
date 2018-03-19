package Perks.Alteration;

import Enumerations.CustomEffectType;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameSystems.CustomEffectSystem;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.Vfx;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class Recover implements IPerk {
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
        int level = PerkSystem.GetPCPerkLevel(oPC, PerkID.Recover);
        int amountMin;
        int amountMax;
        float length;
        int regenAmount;

        switch (level)
        {
            case 1:
                amountMin = 3;
                amountMax = 5;
                length = 18.0f;
                regenAmount = 1;
                break;
            case 2:
                amountMin = 3;
                amountMax = 5;
                length = 30.0f;
                regenAmount = 1;
                break;
            case 3:
                amountMin = 7;
                amountMax = 10;
                length = 30.0f;
                regenAmount = 1;
                break;
            case 4:
                amountMin = 7;
                amountMax = 10;
                length = 30.0f;
                regenAmount = 2;
                break;
            case 5:
                amountMin = 10;
                amountMax = 13;
                length = 30.0f;
                regenAmount = 2;
                break;
            case 6:
                amountMin = 10;
                amountMax = 13;
                length = 30.0f;
                regenAmount = 3;
                break;
            default: return;
        }

        int healAmount = ThreadLocalRandom.current().nextInt(amountMin, amountMax) + 1;

        int luck = PerkSystem.GetPCPerkLevel(oPC, PerkID.Lucky);
        if(ThreadLocalRandom.current().nextInt(100) + 1 <= luck)
        {
            length = length * 2;
        }

        int wisdom = getAbilityModifier(Ability.WISDOM, oPC);
        int intelligence = getAbilityModifier(Ability.INTELLIGENCE, oPC);
        length = length + (wisdom * 4) + (intelligence * 2);

        NWEffect heal = effectHeal(healAmount);
        NWEffect regen = effectRegenerate(regenAmount, 6.0f);
        applyEffectToObject(DurationType.TEMPORARY, regen, oTarget, length);
        applyEffectToObject(DurationType.INSTANT, heal, oTarget, 0.0f);

        SkillSystem.RegisterPCToAllCombatTargetsForSkill(oPC, SkillID.AlterationMagic);

        NWEffect vfx = effectVisualEffect(Vfx.IMP_HEALING_M, false);
        applyEffectToObject(DurationType.INSTANT, vfx, oTarget, 0.0f);
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
