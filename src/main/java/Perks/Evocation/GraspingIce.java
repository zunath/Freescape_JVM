package Perks.Evocation;

import Enumerations.PerkID;
import Enumerations.SkillID;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class GraspingIce implements IPerk {
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
        int level = PerkSystem.GetPCPerkLevel(oPC, PerkID.GraspingIce);
        int damage;
        float slowLength = 0.0f;

        switch(level)
        {
            case 1:
                damage = ThreadLocalRandom.current().nextInt(6) + 1;
                break;
            case 2:
                damage = ThreadLocalRandom.current().nextInt(6) + 1;
                slowLength = 3.0f;
                break;
            case 3:
                damage = ThreadLocalRandom.current().nextInt(6) + 1;
                damage += ThreadLocalRandom.current().nextInt(6) + 1;
                slowLength = 3.0f;
                break;
            case 4:
                damage = ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                slowLength = 3.0f;
                break;
            case 5:
                damage = ThreadLocalRandom.current().nextInt(8) + 1;
                damage += ThreadLocalRandom.current().nextInt(8) + 1;
                damage += ThreadLocalRandom.current().nextInt(8) + 1;
                slowLength = 3.0f;
                break;
            default:
                return;
        }

        int wisdom = getAbilityModifier(Ability.WISDOM, oPC);
        int intelligence = getAbilityModifier(Ability.INTELLIGENCE, oPC);

        float damageMultiplier = 1.0f + (intelligence * 0.2f) + (wisdom * 0.1f);
        damage = (int)((float)damage * damageMultiplier);

        applyEffectToObject(DurationType.INSTANT, effectVisualEffect(VfxFnf.HOWL_MIND, false), oTarget, 0.0f);

        if(slowLength > 0.0f)
        {
            applyEffectToObject(DurationType.TEMPORARY, effectSlow(), oTarget, slowLength);
        }

        applyEffectToObject(DurationType.INSTANT, effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f);
        SkillSystem.RegisterPCToNPCForSkill(oPC, oTarget, SkillID.EvocationMagic);
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
        return true;
    }
}
