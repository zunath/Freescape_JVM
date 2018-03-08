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
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.VfxImp;

// Stuns a single target for a base duration of 1 second.
// Duration is increased by 0.4 seconds per point of Evocation Affinity.
// Duration is increased by 0.75 seconds per point of intelligence beyond 10.
// Duration is increased by 0.2 seconds per point of item bonus.
public class Stun implements IAbility {
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
        int stat = NWScript.getAbilityScore(oPC, Ability.INTELLIGENCE, false) - 10;
        int itemBonus = pcGO.CalculateEvocationBonus();
        float baseDuration = 1.0f;
        float bonusDuration = (skill * 0.40f) + (stat * 0.75f) + (itemBonus * 0.20f);
        float duration = baseDuration + bonusDuration;

        NWEffect effect = NWScript.effectStunned();
        NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oTarget, duration);
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(VfxImp.STUN, false), oTarget, 0.0f);
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
