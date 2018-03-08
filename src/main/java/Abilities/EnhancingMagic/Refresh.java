package Abilities.EnhancingMagic;

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

// Recovers mana of a single target for a base amount of 3 ticks (6 seconds per tick).
// 2 mana is restored every tick.
// The number of ticks increases by 1 for each point of Enhancement Affinity.
// The number of ticks increases by 1 for each point of Wisdom beyond 10.
// The number of ticks increases by 1 for every two points of item bonus.
public class Refresh implements IAbility {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        if(!NWScript.getIsPC(oTarget) || NWScript.getIsDM(oTarget)) return false;
        return true;
    }

    @Override
    public String CannotCastSpellMessage() {
        return null;
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.TouchedByEnhancement))
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
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ENHANCEMENT_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        int itemBonus = pcGO.CalculateEnhancementBonus();
        int baseTicks = 3;
        int bonusTicks = skill + wisdom + (itemBonus / 2);
        int ticks = baseTicks + bonusTicks;

        CustomEffectSystem.ApplyCustomEffect(oPC, oTarget, CustomEffectType.Refresh, ticks);
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
