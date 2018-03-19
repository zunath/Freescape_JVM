package CustomEffect;

import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DamagePower;
import org.nwnx.nwnx2.jvm.constants.DamageType;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

public class BleedingEffect implements ICustomEffectHandler {
    @Override
    public void Apply(NWObject oCaster, NWObject oTarget) {

    }

    @Override
    public void Tick(NWObject oCaster, NWObject oTarget) {
        NWLocation location = NWScript.getLocation(oTarget);
        NWObject oBlood = NWScript.createObject(ObjectType.PLACEABLE, "plc_bloodstain", location, false, "");
        NWScript.destroyObject(oBlood, 48.0f);

        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(1, DamageType.MAGICAL, DamagePower.NORMAL), oTarget, 0.0f);
    }

    @Override
    public void WearOff(NWObject oCaster, NWObject oTarget) {

    }
}
