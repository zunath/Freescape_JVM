package Placeable.StructureSystem.ResearchTerminal;

import Common.IScriptEventHandler;
import Data.Repository.StructureRepository;
import Dialog.DialogManager;
import Entities.PCTerritoryFlagStructureEntity;
import Enumerations.StructurePermission;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class OnUsed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastUsedBy();
        int structureID = StructureSystem.GetPlaceableStructureID(objSelf);
        StructureRepository repo = new StructureRepository();
        PCTerritoryFlagStructureEntity structure = repo.GetPCStructureByID(structureID);

        if(StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanAccessResearchSlots, structure.getPcTerritoryFlag().getPcTerritoryFlagID()))
        {
            DialogManager.startConversation(oPC, objSelf, "ResearchTerminal");
        }
        else
        {
            NWScript.floatingTextStringOnCreature("You do not have permission to access this structure.", oPC, false);
        }

    }
}
