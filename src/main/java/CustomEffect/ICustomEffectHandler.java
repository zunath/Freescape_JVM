package CustomEffect;

import org.nwnx.nwnx2.jvm.NWObject;

public interface ICustomEffectHandler {
    void Apply(NWObject oCaster, NWObject oTarget);
    void Tick(NWObject oCaster, NWObject oTarget);
    void WearOff(NWObject oCaster, NWObject oTarget);
}
