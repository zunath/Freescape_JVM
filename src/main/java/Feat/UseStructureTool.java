package Feat;

import Common.IScriptEventHandler;
import Dialog.DialogManager;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class UseStructureTool implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWLocation lTargetLocation = NWScript.getSpellTargetLocation();
        boolean isMovingStructure = StructureSystem.IsPCMovingStructure(oPC);

        if(isMovingStructure)
        {
            StructureSystem.MoveStructure(oPC, lTargetLocation);
        }
        else
        {
            NWScript.setLocalLocation(oPC, "BUILD_TOOL_LOCATION_TARGET", lTargetLocation);
            DialogManager.startConversation(oPC, oPC, "BuildToolMenu");
        }
    }
}
