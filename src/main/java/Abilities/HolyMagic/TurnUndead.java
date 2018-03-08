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
import org.nwnx.nwnx2.jvm.constants.SavingThrow;

// Forces a single target to turn if they fail the will save check.
// Base duration is 6 seconds.
// Base will save DC is 10.
// Will save DC is increased by 1 per point of wisdom
// Duration is increased by 1 second for every point of wisdom.
// Duration is increased by 1 second for every point of item bonus.
// Duration is increased by 1 second for every 3 points of Holy Affinity skill.
public class TurnUndead implements IAbility {
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
        float baseDuration = 6.0f;
        float bonusDuration = ((skill / 3) + wisdom + itemBonus);
        float duration = baseDuration + bonusDuration;
        int willSaveDC = 10 + wisdom;

        NWEffect effect = NWScript.effectTurned();

        if(NWScript.willSave(oTarget, willSaveDC, SavingThrow.TYPE_DIVINE, oPC) == 0)
        {
            NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oTarget, duration);
        }

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
