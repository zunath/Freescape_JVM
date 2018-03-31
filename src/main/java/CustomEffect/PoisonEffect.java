package CustomEffect;

import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.applyEffectToObject;

public class PoisonEffect implements ICustomEffectHandler {
    @Override
    public void Apply(NWObject oCaster, NWObject oTarget) {

    }

    @Override
    public void Tick(NWObject oCaster, NWObject oTarget) {

        int damage = ThreadLocalRandom.current().nextInt(3, 7);
        Scheduler.assignNow(oCaster, () -> applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f));

        NWEffect decreaseAC = NWScript.effectACDecrease(2, Ac.DODGE_BONUS, Ac.VS_DAMAGE_TYPE_ALL);
        Scheduler.assignNow(oCaster, () -> applyEffectToObject(DurationType.TEMPORARY, decreaseAC, oTarget, 6.1f));
        applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectVisualEffect(Vfx.IMP_ACID_S, false), oTarget, 0.0f);
    }

    @Override
    public void WearOff(NWObject oCaster, NWObject oTarget) {

    }
}
