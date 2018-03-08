package Placeable.StructureSystem.TerritoryFlag;

import Dialog.DialogManager;
import Entities.PCTerritoryFlagEntity;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.StructureRepository;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class OnUsed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject site) {
        NWObject oPC = NWScript.getLastUsedBy();
        PlayerGO pcGO = new PlayerGO(oPC);
        StructureRepository repo = new StructureRepository();
        int flagID = StructureSystem.GetTerritoryFlagID(site);
        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(flagID);

        if(!pcGO.getUUID().equals(entity.getPlayerID()) && !NWScript.getIsDM(oPC))
        {
            NWScript.sendMessageToPC(oPC, "Only the owner of the territory may use this.");
        }
        else
        {
            DialogManager.startConversation(oPC, site, "TerritoryFlag");
        }
    }
}
