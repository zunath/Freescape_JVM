package Abilities.EvocationMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameObject.PlayerGO;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.AttackBonus;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.VfxImp;

// Reduces the attack bonus misc of a single target for a base duration of 30 seconds.
// Base amount reduced is 2 points.
// Amount of AC reduced increases by 1 point for every 3 points of item bonus.
// Duration is increased by 6 seconds for each point of Evocation Affinity.
// Duration is increased by 12 seconds for each point of intelligence beyond 10.
// Duration is increased by 3 seconds for each point of item bonus.
public class Blind implements IAbility {
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
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.TouchedByEvocation))
            baseManaCost--;

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
        PlayerGO pcGO = new PlayerGO(oPC);
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_EVOCATION_AFFINITY);
        int intelligence = NWScript.getAbilityScore(oPC, Ability.INTELLIGENCE, false) - 10;
        int itemBonus = pcGO.CalculateEvocationBonus();
        float baseDuration = 30.0f;
        float bonusDuration = (skill * 6.0f) + (intelligence * 12.0f) + (itemBonus * 3.0f);
        float duration = baseDuration + bonusDuration;
        int amount = 2 + (itemBonus / 3);
        NWEffect effect = NWScript.effectAttackDecrease(amount, AttackBonus.MISC);

        NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oTarget, duration);
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(VfxImp.BLIND_DEAF_M, false), oTarget, 0.0f);

    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }

    @Override
    public boolean IsHostile() {
        return true;
    }
}
