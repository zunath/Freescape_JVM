package CustomEffect;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.VfxCom;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class BurningEffect implements ICustomEffectHandler {
    @Override
    public void Tick(NWObject oCaster, NWObject oTarget) {
        int damage = ThreadLocalRandom.current().nextInt(2) + 1;
        applyEffectToObject(DurationType.INSTANT, effectDamage(damage, DamageType.FIRE, DamagePower.NORMAL), oTarget, 0.0f);
        applyEffectToObject(DurationType.INSTANT, effectVisualEffect(VfxCom.HIT_FIRE, false), oTarget, 0.0f);
    }

    @Override
    public void WearOff(NWObject oCaster, NWObject oTarget) {

    }
}
