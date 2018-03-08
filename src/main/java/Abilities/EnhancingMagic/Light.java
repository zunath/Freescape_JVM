package Abilities.EnhancingMagic;

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
import org.nwnx.nwnx2.jvm.constants.VfxDur;

// Grants an aura of light around a target for a base duration of 120 seconds.
// The duration is increased by 20 seconds for each point of holy affinity skill.
// The duration is increased by 20 seconds for each point of wisdom beyond 10.
// The duration is increased by 20 seconds for each point of item bonus.
// The aura range is increased at 5, 10, and 20 item bonus points.
public class Light implements IAbility {
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
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_HOLY_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        int itemBonus = pcGO.CalculateEnhancementBonus();
        float baseDuration = 120.0f;
        float bonusDuration = (skill + wisdom + itemBonus) * 20.0f;
        float duration = baseDuration + bonusDuration;
        int vfxID = VfxDur.LIGHT_WHITE_5;

        if(itemBonus >= 20)
        {
            vfxID = VfxDur.LIGHT_WHITE_20;
        }
        else if(itemBonus >= 10)
        {
            vfxID = VfxDur.LIGHT_WHITE_15;
        }
        else if(itemBonus >= 5)
        {
            vfxID = VfxDur.LIGHT_WHITE_10;
        }

        NWEffect effect = NWScript.effectVisualEffect(vfxID, false);

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
