package CustomEffect;

import GameSystems.DiseaseSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class InfectionOverTimeEffect implements ICustomEffectHandler {
    @Override
    public void run(NWObject oCaster, NWObject oTarget) {

        if(!NWScript.getIsPC(oTarget)) return;

        DiseaseSystem.IncreaseDiseaseLevel(oTarget, ThreadLocalRandom.current().nextInt(5));
    }
}
