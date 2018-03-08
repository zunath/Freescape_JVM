package CustomEffect;

import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class PiercingTossBleedEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {
        NWLocation location = NWScript.getLocation(oTarget);
        NWObject oBlood = NWScript.createObject(ObjectType.PLACEABLE, "zep_bloodstain7", location, false, "");
        NWScript.destroyObject(oBlood, 48.0f);

        int damage = ThreadLocalRandom.current().nextInt(3, 8);

        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f);
    }
}
