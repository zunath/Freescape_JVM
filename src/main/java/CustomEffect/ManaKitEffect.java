package CustomEffect;

import GameSystems.MagicSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.Vfx;

public class ManaKitEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {

        if(!NWScript.getIsPC(oTarget)) return;

        MagicSystem.RestoreMana(oTarget, 2);
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(Vfx.IMP_CHARM, false), oTarget, 0.0f);
    }
}
