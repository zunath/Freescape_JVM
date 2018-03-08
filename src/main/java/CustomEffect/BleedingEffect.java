package CustomEffect;

import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

@SuppressWarnings("unused")
public class BleedingEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {
        NWLocation location = NWScript.getLocation(oTarget);
        NWObject oBlood = NWScript.createObject(ObjectType.PLACEABLE, "zep_bloodstain7", location, false, "");
        NWScript.destroyObject(oBlood, 48.0f);

        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(1, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f);
    }
}
