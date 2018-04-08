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
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.EffectType;
import org.nwnx.nwnx2.jvm.constants.Vfx;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class Purify implements IPerk {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        return getIsPC(oTarget) && !getIsDM(oTarget) && !getIsDMPossessed(oTarget);
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
        int wisdom = NWScript.getAbilityModifier(Ability.WISDOM, oPC);
        int intelligence = NWScript.getAbilityModifier(Ability.INTELLIGENCE, oPC);
        float castingTime = baseCastingTime - ((wisdom * 1.5f) + (intelligence));

        if(castingTime < 1.0f) castingTime = 1.0f;

        return castingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        int level = PerkSystem.GetPCPerkLevel(oPC, PerkID.Purify);
        int luck = PerkSystem.GetPCPerkLevel(oPC, PerkID.Lucky);

        boolean luckBonus = false;
        if(ThreadLocalRandom.current().nextInt(100) + 1 <= luck)
        {
            luckBonus = true;
        }

        if(level >= 1 || luckBonus)
        {
            CustomEffectSystem.RemovePCCustomEffect(oTarget, CustomEffectType.Bleeding);
        }
        if(level >= 2 || luckBonus)
        {
            CustomEffectSystem.RemovePCCustomEffect(oTarget, CustomEffectType.Poison);
        }
        if(level >= 3 || luckBonus)
        {
            CustomEffectSystem.RemovePCCustomEffect(oTarget, CustomEffectType.Burning);
        }

        NWEffect vfx = effectVisualEffect(Vfx.IMP_HEALING_S, false);
        applyEffectToObject(DurationType.INSTANT, vfx, oTarget, 0.0f);


        for(NWEffect effect: getEffects(oTarget))
        {
            int effectType = getEffectType(effect);
            if(effectType == EffectType.POISON || effectType == EffectType.DISEASE)
            {
                removeEffect(oTarget, effect);
            }
        }

        SkillSystem.RegisterPCToAllCombatTargetsForSkill(oPC, SkillID.AlterationMagic);
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
