package CustomEffect;

import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

public class ShockEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {
        NWEffect stunEffect = NWScript.effectStunned();
        float length = (float)ThreadLocalRandom.current().nextDouble(2.0f, 6.0f);

        NWScript.applyEffectToObject(DurationType.TEMPORARY, stunEffect, oTarget, length);
    }
}
