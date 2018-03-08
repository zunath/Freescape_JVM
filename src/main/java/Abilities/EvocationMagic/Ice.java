package Abilities.EvocationMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameObject.PlayerGO;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.concurrent.ThreadLocalRandom;


// Deals cold damage to a single target and slows them temporarily.
// Minimum damage = 1 + (evocation affinity + intelligence + (item bonus * 4)) * 0.10
// Maximum damage = 4 + (evocation affinity + intelligence + (item bonus * 4)) * 0.25
// Slow length is minimum of 2 seconds.
// Slow length is increased by 0.5 seconds per evocation affinity skill.
// Slow length is increased by 0.75 seconds per intelligence point past 10.
public class Ice implements IAbility {
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
    public void OnImpact(final NWObject oPC, final NWObject oTarget) {
        PlayerGO pcGO = new PlayerGO(oPC);
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_EVOCATION_AFFINITY);
        int intelligence = NWScript.getAbilityScore(oPC, Ability.INTELLIGENCE, false) - 10;
        int itemBonus = pcGO.CalculateEvocationBonus();
        int minimumDamage = 1 + (int)((skill + intelligence + (itemBonus * 4)) * 0.10f);
        int maximumDamage = 4 + (int)((skill + intelligence + (itemBonus * 4)) * 0.25f);
        final int damage = ThreadLocalRandom.current().nextInt(minimumDamage, maximumDamage + 1);

        float slowLength = 2.0f + (skill * 0.5f) + (intelligence * 0.75f);

        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(VfxFnf.ICESTORM, false), oTarget, 0.0f);

        Scheduler.delay(oPC, 50, () -> NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectDamage(damage, DamageType.COLD, DamagePower.NORMAL), oTarget, 0.0f));

        NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectSlow(), oTarget, slowLength);
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
