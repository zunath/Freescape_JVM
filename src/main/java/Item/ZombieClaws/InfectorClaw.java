package Item.ZombieClaws;

import GameSystems.*;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class InfectorClaw implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oZombie) {
        NWObject oPC = NWScript.getSpellTargetObject();
        NWObject oBite = NWScript.getSpellCastItem();
        String itemTag = NWScript.getTag(oBite);
        if (!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        DiseaseSystem.RunDiseaseDCCheck(oZombie, oPC, 65, 5, 5);
    }
}