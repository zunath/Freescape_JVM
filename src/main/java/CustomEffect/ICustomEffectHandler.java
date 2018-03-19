package CustomEffect;

import org.nwnx.nwnx2.jvm.NWObject;

public interface ICustomEffectHandler {
    void Tick(NWObject oCaster, NWObject oTarget);
    void WearOff(NWObject oCaster, NWObject oTarget);
}
