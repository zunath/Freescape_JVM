package Placeable.StructureSystem.PersistentStorage;

import Entities.PCTerritoryFlagStructureEntity;
import Entities.PCTerritoryFlagStructureItemEntity;
import Common.IScriptEventHandler;
import Data.Repository.StructureRepository;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.SCORCO;

@SuppressWarnings("unused")
public class OnOpened implements IScriptEventHandler {
    @Override
    public void runScript(NWObject chest) {
        NWObject oPC = NWScript.getLastOpenedBy();
        int structureID = NWScript.getLocalInt(chest, "STRUCTURE_TEMP_STRUCTURE_ID");
        StructureRepository repo = new StructureRepository();
        PCTerritoryFlagStructureEntity entity = repo.GetPCStructureByID(structureID);
        NWLocation location = NWScript.getLocation(chest);

        for(PCTerritoryFlagStructureItemEntity item : entity.getItems())
        {
            SCORCO.loadObject(item.getItemObject(), location, chest);
        }

        NWScript.setUseableFlag(chest, false);
    }
}
