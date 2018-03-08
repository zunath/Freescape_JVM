package CustomEffect;

import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.concurrent.ThreadLocalRandom;

public class PoisonEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {
        int damage = ThreadLocalRandom.current().nextInt(3, 7);
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f);

        NWEffect decreaseAC = NWScript.effectACDecrease(2, Ac.DODGE_BONUS, Ac.VS_DAMAGE_TYPE_ALL);
        NWScript.applyEffectToObject(DurationType.TEMPORARY, decreaseAC, oTarget, 6.1f);

        NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectVisualEffect(Vfx.IMP_ACID_S, false), oTarget, 0.0f);
    }
}
