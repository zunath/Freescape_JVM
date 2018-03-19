package CustomEffect;

import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class AegisEffect implements ICustomEffectHandler {
    @Override
    public void Apply(NWObject oCaster, NWObject oTarget) {

    }

    @Override
    public void Tick(NWObject oCaster, NWObject oTarget) {
    }

    @Override
    public void WearOff(NWObject oCaster, NWObject oTarget) {
        SkillSystem.ApplyStatChanges(oTarget, null);
    }
}
