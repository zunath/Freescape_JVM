package Perks.Evocation;

import Enumerations.PerkID;
import GameSystems.PerkSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class LightningShock implements IPerk {
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
        int level = PerkSystem.GetPCPerkLevel(oPC, PerkID.LightningShock);
        int damage;

        switch(level)
        {
            case 1:
                damage = ThreadLocalRandom.current().nextInt(8) + 1;
                break;
            case 2:
                damage = ThreadLocalRandom.current().nextInt(6) + 1;
                damage += ThreadLocalRandom.current().nextInt(6) + 1;
                break;
            case 3:
                damage = ThreadLocalRandom.current().nextInt(6) + 1;
                damage += ThreadLocalRandom.current().nextInt(6) + 1;
                break;
            case 4:
                damage = ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                break;
            case 5:
                damage = ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                damage += ThreadLocalRandom.current().nextInt(4) + 1;
                break;
            default:
                return;
        }

        applyEffectToObject(DurationType.INSTANT, effectVisualEffect(VfxImp.DOOM, false), oTarget, 0.0f);
        applyEffectToObject(DurationType.INSTANT, effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f);
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
