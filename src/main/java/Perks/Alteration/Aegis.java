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

public class Aegis implements IPerk {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        int level = PerkSystem.GetPCPerkLevel(oPC, PerkID.Aegis);
        int activeAegisLevel = CustomEffectSystem.GetActiveEffectLevel(oTarget, CustomEffectType.Aegis);

        return level >= activeAegisLevel;
    }

    @Override
    public String CannotCastSpellMessage() {
        return "A more powerful effect already exists on your target.";
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
        int level = PerkSystem.GetPCPerkLevel(oPC, PerkID.Aegis);
        int ticks;

        switch (level)
        {
            case 1:
            case 2:
                ticks = 10;
                break;
            case 3:
            case 4:
            case 5:
                ticks = 50;
                break;
            default: return;
        }

        int luck = PerkSystem.GetPCPerkLevel(oPC, PerkID.Lucky);
        if(ThreadLocalRandom.current().nextInt(100) + 1 <= luck)
        {
            ticks = ticks * 2;
        }

        int wisdom = getAbilityModifier(Ability.WISDOM, oPC);
        int intelligence = getAbilityModifier(Ability.INTELLIGENCE, oPC);
        ticks = ticks + intelligence + (wisdom * 2);

        CustomEffectSystem.ApplyCustomEffect(oPC, oTarget, CustomEffectType.Aegis, ticks, level);
        SkillSystem.ApplyStatChanges(oTarget, null);
        SkillSystem.RegisterPCToAllCombatTargetsForSkill(oPC, SkillID.AlterationMagic);

        NWEffect vfx = effectVisualEffect(Vfx.IMP_AC_BONUS, false);
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
