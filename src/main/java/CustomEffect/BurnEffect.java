package CustomEffect;

import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

public class BurnEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {
        int damage = ThreadLocalRandom.current().nextInt(3, 5);
        NWEffect damageEffect = NWScript.effectDamage(damage, DamageType.FIRE, DamagePower.NORMAL);

        NWScript.applyEffectToObject(DurationType.INSTANT, damageEffect, oTarget, 0.0f);
    }
}
