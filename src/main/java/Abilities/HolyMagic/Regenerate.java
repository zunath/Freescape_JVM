package Abilities.HolyMagic;

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

// Grants an HP regeneration effect to a single target for a base duration of 60 seconds.
//      Interval: Every 10 seconds
//      Amount per interval: 1 Hit Point
// Duration is increased by 20 seconds for every two points of Holy Affinity skill.
// Duration is increased by 20 seconds for every point of wisdom beyond 10.
// Duration is increased by 20 seconds for every point of item bonus.
public class Regenerate implements IAbility {
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
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.TouchedByHoly))
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
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_HOLY_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        int itemBonus = pcGO.CalculateHolyBonus();
        float baseDuration = 60.0f;
        float bonusDuration = ((skill / 2) + wisdom + itemBonus) * 20.0f;
        float duration = baseDuration + bonusDuration;

        int amount = 1;
        float interval = 10.0f;

        if(wisdom >= 3) amount++;
        if(wisdom >= 6) interval = 5.0f;

        NWEffect effect = NWScript.effectRegenerate(amount, interval);

        NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oTarget, duration);
    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
