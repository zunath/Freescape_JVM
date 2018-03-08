package Abilities.HolyMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import Enumerations.CustomEffectType;
import GameObject.PlayerGO;
import GameSystems.CustomEffectSystem;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;

// Removes the custom poison effect from a single target.
// Casting time is reduced by 0.5 seconds for every point of Holy Affinity.
// Casting time is reduced by 0.5 seconds for every point of item bonus.
// Casting time is reduced by 0.5 seconds for every point of wisdom beyond 10.
// Minimum casting time is 0.5 seconds.
public class Antidote implements IAbility {
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
        PlayerGO pcGO = new PlayerGO(oPC);
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_HOLY_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        int itemBonus = pcGO.CalculateHolyBonus();
        float castingTimeReduction = (skill + itemBonus + wisdom) * 0.5f;
        float castingTime = baseCastingTime - castingTimeReduction;

        if(castingTime < 0.5f)
            castingTime = 0.5f;

        return castingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }


    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {

        if(!CustomEffectSystem.DoesPCHaveCustomEffect(oTarget, CustomEffectType.Poison))
        {
            NWScript.sendMessageToPC(oPC, "Your target is not afflicted by poison.");
            return;
        }

        CustomEffectSystem.RemovePCCustomEffect(oTarget, CustomEffectType.Poison);
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
