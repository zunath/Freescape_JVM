package Perks.Evocation;

import Enumerations.CustomEffectType;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameSystems.CustomEffectSystem;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class FireBlast implements IPerk {
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
        int level = PerkSystem.GetPCPerkLevel(oPC, PerkID.FireBlast);
        int damage;
        int ticks = 0;

        switch(level)
        {
            case 1:
                damage = ThreadLocalRandom.current().nextInt(6) + 1;
                break;
            case 2:
                damage = ThreadLocalRandom.current().nextInt(6) + 1;
                ticks = 3;
                break;
            case 3:
                damage = ThreadLocalRandom.current().nextInt(6) + 1;
                damage += ThreadLocalRandom.current().nextInt(6) + 1;
                ticks = 4;
                break;
            case 4:
                damage = ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                ticks = 4;
                break;
            case 5:
                damage = ThreadLocalRandom.current().nextInt(8) + 1;
                damage += ThreadLocalRandom.current().nextInt(8) + 1;
                damage += ThreadLocalRandom.current().nextInt(8) + 1;
                ticks = 5;
                break;
            default:
                return;
        }

        int wisdom = getAbilityModifier(Ability.WISDOM, oPC);
        int intelligence = getAbilityModifier(Ability.INTELLIGENCE, oPC);

        float damageMultiplier = 1.0f + (intelligence * 0.2f) + (wisdom * 0.1f);
        damage = (int)((float)damage * damageMultiplier);

        applyEffectToObject(DurationType.INSTANT, effectVisualEffect(Vfx.COM_HIT_FIRE, false), oTarget, 0.0f);

        if(ticks > 0)
        {
            CustomEffectSystem.ApplyCustomEffect(oPC, oTarget, CustomEffectType.Burning, ticks, level);
        }

        SkillSystem.RegisterPCToNPCForSkill(oPC, oTarget, SkillID.EvocationMagic);
        applyEffectToObject(DurationType.INSTANT, effectDamage(damage, DamageType.FIRE, DamagePower.NORMAL), oTarget, 0.0f);
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
