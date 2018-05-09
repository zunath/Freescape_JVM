package Placeable.StructureSystem.BuildingDoor;

import Common.IScriptEventHandler;
import Conversation.ConstructionSite;
import Data.Repository.PlayerRepository;
import Data.Repository.StructureRepository;
import Entities.*;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnUsed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject door) {
        NWObject oPC = getLastUsedBy();
        int structureID = StructureSystem.GetPlaceableStructureID(door);

        if(structureID <= 0)
        {
            floatingTextStringOnCreature("ERROR: Door doesn't have a structure ID assigned. Notify an admin about this issue.", oPC, false);
            return;
        }

        StructureRepository structureRepo = new StructureRepository();
        PCTerritoryFlagStructureEntity structure = structureRepo.GetPCStructureByID(structureID);
        BuildingInteriorEntity interior = structure.getBuildingInterior();

        if(interior == null)
        {
            floatingTextStringOnCreature("ERROR: Could not locate interior object for structure. Notify an admin about this issue.", oPC, false);
            return;
        }

        NWObject instance = null;
        NWObject area = getFirstArea();
        while(getIsObjectValid(area))
        {
            if(getLocalInt(area, "BUILDING_STRUCTURE_ID") == structureID)
            {
                instance = area;
                break;
            }

            area = getNextArea();
        }

        if(instance == null)
        {
            PCTerritoryFlagEntity flag = structureRepo.GetPCTerritoryFlagByBuildingStructureID(structureID);

            String name = structure.getCustomName();
            if(name.equals(""))
            {
                PlayerRepository playerRepo = new PlayerRepository();
                PlayerEntity owner = playerRepo.GetByPlayerID(structure.getPcTerritoryFlag().getPlayerID());
                name = owner.getCharacterName() + "'s Building";
            }

            instance = createArea(interior.getAreaResref(), "", name);
            setLocalInt(instance, "BUILDING_STRUCTURE_ID", structureID);
            setLocalInt(instance, "TERRITORY_FLAG_ID", flag.getPcTerritoryFlagID());

            // Load structures & construction sites
            for(ConstructionSiteEntity entity: flag.getConstructionSites())
            {
                StructureSystem.CreateConstructionSiteFromEntity(entity);
            }
            for(PCTerritoryFlagStructureEntity entity: flag.getStructures())
            {
                StructureSystem.CreateStructureFromEntity(entity);
            }

        }

        setLocalInt(instance, "BUILDING_DISABLED", 0);
        StructureSystem.JumpPCToBuildingInterior(oPC, instance);
    }

}
