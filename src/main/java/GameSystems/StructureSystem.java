package GameSystems;

import Data.Repository.PlayerRepository;
import Data.Repository.StructureRepository;
import Entities.*;
import Enumerations.QuestID;
import Enumerations.StructurePermission;
import GameObject.PlayerGO;
import Helper.ColorToken;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Duration;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class StructureSystem {

    // Variable names
    private static final String TerritoryFlagIDVariableName = "TERRITORY_FLAG_ID";
    private static final String StructureIDVariableName = "TERRITORY_STRUCTURE_SITE_ID";
    private static final String ConstructionSiteIDVariableName = "CONSTRUCTION_SITE_ID";

    private static final String IsMovingStructureLocationVariableName = "IS_MOVING_STRUCTURE_LOCATION";
    private static final String MovingStructureVariableName = "MOVING_STRUCTURE_OBJECT";

    // Resrefs
    private static final String ConstructionSiteResref = "const_site";
    private static final String TemporaryLocationCheckerObjectResref = "temp_loc_check";


    public static void OnModuleLoad()
    {
        StructureRepository repo = new StructureRepository();
        PlayerRepository playerRepo = new PlayerRepository();

        List<ConstructionSiteEntity> constructionSites = repo.GetAllConstructionSites();
        for(ConstructionSiteEntity entity : constructionSites)
        {
            NWObject oArea = NWScript.getObjectByTag(entity.getLocationAreaTag(), 0);
            NWVector position = NWScript.vector((float) entity.getLocationX(), (float) entity.getLocationY(), (float) entity.getLocationZ());
            NWLocation location = NWScript.location(oArea, position, (float) entity.getLocationOrientation());

            NWObject constructionSite = NWScript.createObject(ObjectType.PLACEABLE, ConstructionSiteResref, location, false, "");
            NWScript.setLocalInt(constructionSite, ConstructionSiteIDVariableName, entity.getConstructionSiteID());
            NWScript.setName(constructionSite, "Construction Site: " + entity.getBlueprint().getName());

            NWEffect eGhostWalk = NWScript.effectCutsceneGhost();
            NWScript.applyEffectToObject(Duration.TYPE_PERMANENT, eGhostWalk, constructionSite, 0.0f);
        }

        List<PCTerritoryFlagEntity> territoryFlags = repo.GetAllTerritoryFlags();
        for(PCTerritoryFlagEntity flag : territoryFlags)
        {
            NWObject oArea = NWScript.getObjectByTag(flag.getLocationAreaTag(), 0);
            NWVector position = NWScript.vector((float) flag.getLocationX(), (float) flag.getLocationY(), (float) flag.getLocationZ());
            NWLocation location = NWScript.location(oArea, position, (float) flag.getLocationOrientation());
            PlayerEntity playerEntity = playerRepo.GetByPlayerID(flag.getPlayerID());

            NWObject territoryFlag = NWScript.createObject(ObjectType.PLACEABLE, flag.getBlueprint().getResref(), location, false, "");
            NWScript.setLocalInt(territoryFlag, TerritoryFlagIDVariableName, flag.getPcTerritoryFlagID());
            NWScript.setName(territoryFlag, playerEntity.getCharacterName() + "'s Territory");

            for(PCTerritoryFlagStructureEntity structure : flag.getStructures())
            {
                oArea = NWScript.getObjectByTag(structure.getLocationAreaTag(), 0);
                position = NWScript.vector((float) structure.getLocationX(), (float) structure.getLocationY(), (float) structure.getLocationZ());
                location = NWScript.location(oArea, position, (float) structure.getLocationOrientation());

                NWObject structurePlaceable = NWScript.createObject(ObjectType.PLACEABLE, structure.getBlueprint().getResref(), location, false, "");
                NWScript.setLocalInt(structurePlaceable, StructureIDVariableName, structure.getPcTerritoryFlagStructureID());
                NWScript.setPlotFlag(structurePlaceable, true);
                NWScript.setUseableFlag(structurePlaceable, structure.isUseable());

                if(structure.getBlueprint().getItemStorageCount() > 0)
                {
                    NWScript.setName(structurePlaceable, NWScript.getName(structurePlaceable, false) + " (" + structure.getBlueprint().getItemStorageCount() + " items)");
                }
            }

        }
    }

    public static boolean IsPCMovingStructure(NWObject oPC)
    {
        return NWScript.getLocalInt(oPC, IsMovingStructureLocationVariableName) == 1 &&
                !NWScript.getLocalObject(oPC, MovingStructureVariableName).equals(NWObject.INVALID);
    }

    public static void SetIsPCMovingStructure(NWObject oPC, NWObject structure, boolean isMoving)
    {
        if(isMoving)
        {
            NWScript.setLocalInt(oPC, IsMovingStructureLocationVariableName, 1);
            NWScript.setLocalObject(oPC, MovingStructureVariableName, structure);
        }
        else
        {
            NWScript.deleteLocalInt(oPC, IsMovingStructureLocationVariableName);
            NWScript.deleteLocalObject(oPC, MovingStructureVariableName);
        }
    }

    private static void SetPlaceableStructureID(NWObject structure, int structureID)
    {
        NWScript.setLocalInt(structure, StructureIDVariableName, structureID);
    }

    public static int GetPlaceableStructureID(NWObject structure)
    {
        return NWScript.getLocalInt(structure, StructureIDVariableName) <= 0 ?
                -1 :
                NWScript.getLocalInt(structure, StructureIDVariableName);
    }

    // Assumption: There will never be an overlap of two or more territory flags' areas of influence.
    // If no territory flags own the location, NWObject.INVALID is returned.
    public static NWObject GetTerritoryFlagOwnerOfLocation(NWLocation location)
    {
        StructureRepository repo = new StructureRepository();
        String areaTag = NWScript.getTag(location.getArea());
        List<PCTerritoryFlagEntity> areaFlags = repo.GetAllFlagsInArea(areaTag);
        NWObject placeable = NWObject.INVALID;

        for(PCTerritoryFlagEntity flag : areaFlags)
        {
            NWLocation flagLocation = new NWLocation(
                    location.getArea(),
                    (float)flag.getLocationX(),
                    (float)flag.getLocationY(),
                    (float)flag.getLocationZ(),
                    (float)flag.getLocationOrientation()
            );
            float distance = NWScript.getDistanceBetweenLocations(flagLocation, location);

            if(distance <= flag.getBlueprint().getMaxBuildDistance())
            {
                // Found the territory which "owns" this location. Look for the flag placeable with a matching ID.
                int currentPlaceable = 1;
                NWObject checker = NWScript.createObject(ObjectType.PLACEABLE, TemporaryLocationCheckerObjectResref, location, false, "");

                do
                {
                    placeable = NWScript.getNearestObject(ObjectType.PLACEABLE, checker, currentPlaceable);

                    if(GetTerritoryFlagID(placeable) == flag.getPcTerritoryFlagID())
                    {
                        break;
                    }

                    currentPlaceable++;
                } while(!placeable.equals(NWObject.INVALID));


                NWScript.destroyObject(checker, 0.0f);

            }

        }
        return placeable;
    }

    public static int CanPCBuildInLocation(NWObject oPC, NWLocation targetLocation, int permissionCheck)
    {
        StructureRepository repo = new StructureRepository();
        NWObject flag = GetTerritoryFlagOwnerOfLocation(targetLocation);
        NWLocation flagLocation = NWScript.getLocation(flag);
        int pcTerritoryFlagID = GetTerritoryFlagID(flag);
        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(pcTerritoryFlagID);
        PlayerGO pcGO = new PlayerGO(oPC);
        float distance = NWScript.getDistanceBetweenLocations(flagLocation, targetLocation);

        if(flag.equals(NWObject.INVALID) ||
                distance > entity.getBlueprint().getMaxBuildDistance())
        {
            return 1;
        }

        if(repo.GetNumberOfStructuresInTerritory(pcTerritoryFlagID) >=
                entity.getBlueprint().getMaxStructuresCount())
        {
            return 2;
        }

        if(entity.getPlayerID().equals(pcGO.getUUID()))
        {
            return 1;
        }

        if(PlayerHasPermission(oPC, permissionCheck, pcTerritoryFlagID))
        {
            return 1;
        }

        return 0;
    }


    private static boolean IsWithinRangeOfTerritoryFlag(NWObject oCheck)
    {
        NWLocation location = NWScript.getLocation(oCheck);
        NWObject flag = GetTerritoryFlagOwnerOfLocation(location);

        if(flag.equals(NWObject.INVALID)) return false;

        float distance = NWScript.getDistanceBetween(oCheck, flag);
        StructureRepository repo = new StructureRepository();
        int flagID = GetTerritoryFlagID(flag);
        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(flagID);

        return distance <= entity.getBlueprint().getMaxBuildDistance();
    }

    public static void CreateConstructionSite(NWObject oPC, NWLocation location)
    {
        int buildStatus = CanPCBuildInLocation(oPC, location, StructurePermission.CanBuildStructures);

        if(buildStatus == 0) // 0 = Can't do it in that location
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You can't build a construction site there." + ColorToken.End(), oPC, false);
        }
        else if(buildStatus == 1) // 1 = Success
        {
            NWObject constructionSite = NWScript.createObject(ObjectType.PLACEABLE, ConstructionSiteResref, location, false, "");
            NWEffect eGhostWalk = NWScript.effectCutsceneGhost();
            NWScript.applyEffectToObject(Duration.TYPE_PERMANENT, eGhostWalk, constructionSite, 0.0f);

            if(QuestSystem.GetPlayerQuestJournalID(oPC, QuestID.BootCampBuildingStructures) == 1)
            {
                QuestSystem.AdvanceQuestState(oPC, QuestID.BootCampBuildingStructures);
            }

            NWScript.floatingTextStringOnCreature("Construction site created! Use the construction site to select a blueprint.", oPC, false);
        }
        else if(buildStatus == 2) // 2 = Territory can't hold any more structures.
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "The maximum number of structures this territory can manage has been reached. Raze a structure or upgrade your territory to create a new structure." + ColorToken.End(), oPC, false);
        }
    }

    public static int GetConstructionSiteID(NWObject site)
    {
        return NWScript.getLocalInt(site, ConstructionSiteIDVariableName);
    }

    private static void SetConstructionSiteID(NWObject site, int constructionSiteID)
    {
        NWScript.setLocalInt(site, ConstructionSiteIDVariableName, constructionSiteID);
    }

    public static int GetTerritoryFlagID(NWObject flag)
    {
        return NWScript.getLocalInt(flag, TerritoryFlagIDVariableName);
    }

    public static void SelectBlueprint(NWObject oPC, NWObject constructionSite, int blueprintID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        StructureRepository repo = new StructureRepository();
        ConstructionSiteEntity entity = new ConstructionSiteEntity();
        NWObject area = NWScript.getArea(constructionSite);
        String areaTag = NWScript.getTag(area);
        NWLocation location = NWScript.getLocation(constructionSite);
        StructureBlueprintEntity blueprint = repo.GetStructureBlueprintByID(blueprintID);

        entity.setLocationAreaTag(areaTag);
        entity.setLocationOrientation(location.getFacing());
        entity.setLocationX(location.getX());
        entity.setLocationY(location.getY());
        entity.setLocationZ(location.getZ());
        entity.setPlayerID(pcGO.getUUID());
        entity.setBlueprint(blueprint);
        entity.setClothRequired(blueprint.getClothRequired());
        entity.setLeatherRequired(blueprint.getLeatherRequired());
        entity.setMetalRequired(blueprint.getMetalRequired());
        entity.setNailsRequired(blueprint.getNailsRequired());
        entity.setWoodRequired(blueprint.getWoodRequired());
        entity.setIronRequired(blueprint.getIronRequired());

        if(IsWithinRangeOfTerritoryFlag(constructionSite))
        {
            NWObject flag = GetTerritoryFlagOwnerOfLocation(location);
            PCTerritoryFlagEntity flagEntity = repo.GetPCTerritoryFlagByID(GetTerritoryFlagID(flag));
            entity.setPcTerritoryFlag(flagEntity);
        }

        repo.Save(entity);
        SetConstructionSiteID(constructionSite, entity.getConstructionSiteID());
        NWScript.setName(constructionSite, "Construction Site: " + entity.getBlueprint().getName());
    }

    public static void MoveStructure(NWObject oPC, NWLocation location)
    {
        StructureRepository repo = new StructureRepository();
        NWObject target = NWScript.getLocalObject(oPC, MovingStructureVariableName);
        NWObject nearestFlag = GetTerritoryFlagOwnerOfLocation(location);
        NWLocation nearestFlagLocation = NWScript.getLocation(nearestFlag);
        int nearestFlagID = GetTerritoryFlagID(nearestFlag);
        boolean outsideOwnFlagRadius = false;
        int constructionSiteID = GetConstructionSiteID(target);
        int structureID = GetPlaceableStructureID(target);
        NWScript.deleteLocalInt(oPC, IsMovingStructureLocationVariableName);
        NWScript.deleteLocalObject(oPC, MovingStructureVariableName);

        if(target.equals(NWObject.INVALID) ||
                !location.getArea().equals(NWScript.getArea(target))) {
            return;
        }

        if(!PlayerHasPermission(oPC, StructurePermission.CanMoveStructures, nearestFlagID))
        {
            NWScript.floatingTextStringOnCreature("You do not have permission to move this structure.", oPC, false);
            return;
        }

        // Moving construction site, no blueprint set
        if(constructionSiteID <= 0 && NWScript.getResRef(target).equals(ConstructionSiteResref))
        {
            if(CanPCBuildInLocation(oPC, location, StructurePermission.CanMoveStructures) == 0)
            {
                outsideOwnFlagRadius = true;
            }
        }

        // Moving construction site, blueprint is set.
        if(constructionSiteID > 0)
        {
            ConstructionSiteEntity entity = repo.GetConstructionSiteByID(constructionSiteID);
            boolean isTerritoryMarkerConstructionSite = entity.getPcTerritoryFlag() == null;

            // Territory marker - Ensure not in radius of another territory
            if(isTerritoryMarkerConstructionSite )
            {
                PCTerritoryFlagEntity nearestFlagEntity = repo.GetPCTerritoryFlagByID(nearestFlagID);
                if(nearestFlagEntity != null && NWScript.getDistanceBetweenLocations(location, nearestFlagLocation) <= nearestFlagEntity.getBlueprint().getMaxBuildDistance())
                {
                    NWScript.floatingTextStringOnCreature("Cannot move territory markers within the building range of another territory marker.", oPC, false);
                    return;
                }
            }
            else if(entity.getPcTerritoryFlag().getPcTerritoryFlagID() != nearestFlagID ||
                    NWScript.getDistanceBetweenLocations(nearestFlagLocation, location) > entity.getPcTerritoryFlag().getBlueprint().getMaxBuildDistance())
            {
                outsideOwnFlagRadius = true;
            }
            else
            {
                entity.setLocationOrientation(location.getFacing());
                entity.setLocationX(location.getX());
                entity.setLocationY(location.getY());
                entity.setLocationZ(location.getZ());

                repo.Save(entity);
            }
        }
        else if(structureID > 0)
        {
            PCTerritoryFlagStructureEntity entity = repo.GetPCStructureByID(structureID);

            if(entity.getPcTerritoryFlag().getPcTerritoryFlagID() != nearestFlagID ||
                    NWScript.getDistanceBetweenLocations(nearestFlagLocation, location) > entity.getPcTerritoryFlag().getBlueprint().getMaxBuildDistance())
            {
                outsideOwnFlagRadius = true;
            }
            else
            {
                entity.setLocationOrientation(location.getFacing());
                entity.setLocationX(location.getX());
                entity.setLocationY(location.getY());
                entity.setLocationZ(location.getZ());

                repo.Save(entity);
            }
        }

        if(outsideOwnFlagRadius)
        {
            NWScript.floatingTextStringOnCreature("Unable to move structure to that location. New location must be within range of the territory marker it is attached to.", oPC, false);
            return;
        }
        NWObject copy = NWScript.createObject(ObjectType.PLACEABLE, NWScript.getResRef(target), location, false, "");
        NWScript.setName(copy, NWScript.getName(target, false));

        if(constructionSiteID > 0) SetConstructionSiteID(copy, constructionSiteID);
        else if (structureID > 0) SetPlaceableStructureID(copy, structureID);

        NWScript.destroyObject(NWScript.getLocalObject(target, "GateBlock"), 0.0f);
        NWScript.destroyObject(target, 0.0f);
    }

    public static void LogQuickBuildAction(NWObject oDM, NWObject completedStructure)
    {
        if(!NWScript.getIsPC(oDM) && !NWScript.getIsDM(oDM)) return;

        StructureRepository repo = new StructureRepository();
        String name = NWScript.getName(oDM, false);
        int flagID = NWScript.getLocalInt(completedStructure, TerritoryFlagIDVariableName);
        long structureID = (long)NWScript.getLocalInt(completedStructure, StructureIDVariableName);

        StructureQuickBuildAuditEntity audit = new StructureQuickBuildAuditEntity();
        audit.setDateBuilt(new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
        audit.setDmName(name);

        if(flagID > 0)
            audit.setPcTerritoryFlagID(flagID);
        else
            audit.setPcTerritoryFlagID(null);

        if(structureID > 0)
            audit.setPcTerritoryFlagStructureID(structureID);
        else
            audit.setPcTerritoryFlagStructureID(null);

        repo.Save(audit);
    }

    public static NWObject CompleteStructure(NWObject constructionSite)
    {
        StructureRepository repo = new StructureRepository();
        int constructionSiteID = GetConstructionSiteID(constructionSite);
        ConstructionSiteEntity entity = repo.GetConstructionSiteByID(constructionSiteID);
        StructureBlueprintEntity blueprint = entity.getBlueprint();
        NWLocation location = NWScript.getLocation(constructionSite);

        NWObject structurePlaceable = NWScript.createObject(ObjectType.PLACEABLE, blueprint.getResref(), location, false, "");
        NWScript.destroyObject(constructionSite, 0.0f);

        if(blueprint.isTerritoryFlag())
        {
            PlayerRepository playerRepo = new PlayerRepository();
            PlayerEntity playerEntity = playerRepo.GetByPlayerID(entity.getPlayerID());
            NWScript.setName(structurePlaceable, playerEntity.getCharacterName() + "'s Territory");

            PCTerritoryFlagEntity pcFlag = new PCTerritoryFlagEntity();
            pcFlag.setBlueprint(blueprint);
            pcFlag.setLocationAreaTag(entity.getLocationAreaTag());
            pcFlag.setLocationOrientation(entity.getLocationOrientation());
            pcFlag.setLocationX(entity.getLocationX());
            pcFlag.setLocationY(entity.getLocationY());
            pcFlag.setLocationZ(entity.getLocationZ());
            pcFlag.setPlayerID(entity.getPlayerID());

            repo.Save(pcFlag);
            NWScript.setLocalInt(structurePlaceable, TerritoryFlagIDVariableName, pcFlag.getPcTerritoryFlagID());
        }
        else
        {
            PCTerritoryFlagStructureEntity pcStructure = new PCTerritoryFlagStructureEntity();
            pcStructure.setBlueprint(blueprint);
            pcStructure.setLocationAreaTag(entity.getLocationAreaTag());
            pcStructure.setLocationOrientation(entity.getLocationOrientation());
            pcStructure.setLocationX(entity.getLocationX());
            pcStructure.setLocationY(entity.getLocationY());
            pcStructure.setLocationZ(entity.getLocationZ());
            pcStructure.setPcTerritoryFlag(entity.getPcTerritoryFlag());
            pcStructure.setIsUseable(entity.getBlueprint().isUseable());

            repo.Save(pcStructure);
            NWScript.setLocalInt(structurePlaceable, StructureIDVariableName, pcStructure.getPcTerritoryFlagStructureID());

            if(entity.getBlueprint().getItemStorageCount() > 0)
            {
                NWScript.setName(structurePlaceable, NWScript.getName(structurePlaceable, false) + " (" + entity.getBlueprint().getItemStorageCount() + " items)");
            }
        }

        repo.Delete(entity);

        return structurePlaceable;
    }


    public static void RazeTerritory(NWObject flag)
    {
        int flagID = GetTerritoryFlagID(flag);
        StructureRepository repo = new StructureRepository();
        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(flagID);
        ArrayList<Integer> constructionSiteIDs = new ArrayList<>();
        ArrayList<Integer> structureSiteIDs = new ArrayList<>();

        for(PCTerritoryFlagStructureEntity structure : entity.getStructures())
        {
            if(!structureSiteIDs.contains(structure.getPcTerritoryFlagStructureID()))
            {
                structureSiteIDs.add(structure.getPcTerritoryFlagStructureID());
            }
        }

        for(ConstructionSiteEntity constructionSite : entity.getConstructionSites())
        {
            if(!constructionSiteIDs.contains(constructionSite.getConstructionSiteID()))
            {
                constructionSiteIDs.add(constructionSite.getConstructionSiteID());
            }
        }


        int currentPlaceable = 1;
        NWObject placeable = NWScript.getNearestObject(ObjectType.PLACEABLE, flag, currentPlaceable);
        while(!placeable.equals(NWObject.INVALID))
        {
            if(NWScript.getDistanceBetween(placeable, flag) > entity.getBlueprint().getMaxBuildDistance()) break;

            if(constructionSiteIDs.contains(GetConstructionSiteID(placeable)) ||
                    structureSiteIDs.contains(GetPlaceableStructureID(placeable)))
            {

                NWScript.destroyObject(NWScript.getLocalObject(placeable, "GateBlock"), 0.0f);
                NWScript.destroyObject(placeable, 0.0f);
            }

            currentPlaceable++;
            placeable = NWScript.getNearestObject(ObjectType.PLACEABLE, flag, currentPlaceable);
        }

        NWScript.destroyObject(flag, 0.0f);
        repo.Delete(entity);
    }

    public static void TransferTerritoryOwnership(NWObject oFlag, String newOwnerUUID)
    {
        PlayerRepository playerRepo = new PlayerRepository();
        StructureRepository repo = new StructureRepository();
        int pcFlagID = GetTerritoryFlagID(oFlag);
        PlayerEntity playerEntity = playerRepo.GetByPlayerID(newOwnerUUID);
        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(pcFlagID);
        entity.getPermissions().clear();
        entity.setPlayerID(newOwnerUUID);
        repo.Save(entity);

        NWScript.setName(oFlag, playerEntity.getCharacterName() + "'s Territory");

        for(NWObject oPC : NWScript.getPCs())
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            if(pcGO.getUUID().equals(newOwnerUUID))
            {
                NWScript.floatingTextStringOnCreature("Ownership of a territory in " + NWScript.getName(NWScript.getArea(oFlag), false)  + " has been transferred to you.", oPC, false);
                break;
            }
        }
    }

    public static void RazeConstructionSite(NWObject oPC, NWObject site, boolean recoverMaterials)
    {
        int constructionSiteID = GetConstructionSiteID(site);
        if(constructionSiteID > 0)
        {
            StructureRepository repo = new StructureRepository();
            ConstructionSiteEntity entity = repo.GetConstructionSiteByID(constructionSiteID);

            if(recoverMaterials)
            {
                int wood = entity.getBlueprint().getWoodRequired() - entity.getWoodRequired();
                int metal = entity.getBlueprint().getMetalRequired() - entity.getMetalRequired();
                int nails = entity.getBlueprint().getNailsRequired() - entity.getNailsRequired();
                int cloth = entity.getBlueprint().getClothRequired() - entity.getClothRequired();
                int leather = entity.getBlueprint().getLeatherRequired() - entity.getLeatherRequired();
                int iron = entity.getBlueprint().getIronRequired() - entity.getIronRequired();

                for(int w = 1; w <= wood; w++) NWScript.createItemOnObject("reo_wood", oPC, 1, "");
                for(int m = 1; m <= metal; m++) NWScript.createItemOnObject("reo_metal", oPC, 1, "");
                for(int n = 1; n <= nails; n++) NWScript.createItemOnObject("reo_nails", oPC, 1, "");
                for(int c = 1; c <= cloth; c++) NWScript.createItemOnObject("reo_cloth", oPC, 1, "");
                for(int l = 1; l <= leather; l++) NWScript.createItemOnObject("reo_leather", oPC, 1, "");
                for(int i = 1; i <= iron; i++) NWScript.createItemOnObject("reo_iron", oPC, 1, "");

            }

            repo.Delete(entity);
        }
        NWScript.destroyObject(site, 0.0f);
    }

    public static boolean WillBlueprintOverlapWithExistingFlags(NWLocation location, int blueprintID)
    {
        StructureRepository repo = new StructureRepository();
        NWObject area = NWScript.getAreaFromLocation(location);
        List<PCTerritoryFlagEntity> flags = repo.GetAllFlagsInArea(NWScript.getTag(area));
        StructureBlueprintEntity blueprint = repo.GetStructureBlueprintByID(blueprintID);

        for(PCTerritoryFlagEntity flag : flags)
        {
            NWLocation flagLocation = new NWLocation(
                    location.getArea(),
                    (float)flag.getLocationX(),
                    (float)flag.getLocationY(),
                    (float)flag.getLocationZ(),
                    (float)flag.getLocationOrientation()
            );

            float distance = NWScript.getDistanceBetweenLocations(location, flagLocation);
            float overlapDistance = (float)flag.getBlueprint().getMaxBuildDistance() + (float)blueprint.getMaxBuildDistance();

            if(distance <= overlapDistance)
                return true;

        }

        return false;
    }

    public static boolean IsConstructionSiteValid(NWObject site)
    {
        StructureRepository repo = new StructureRepository();
        NWLocation siteLocation = NWScript.getLocation(site);
        NWObject flag = GetTerritoryFlagOwnerOfLocation(siteLocation);
        NWLocation flaglocation = NWScript.getLocation(flag);
        int flagID = StructureSystem.GetTerritoryFlagID(flag);
        int constructionSiteID = StructureSystem.GetConstructionSiteID(site);
        if(flagID <= 0) return true;

        ConstructionSiteEntity constructionSiteEntity = repo.GetConstructionSiteByID(constructionSiteID);
        PCTerritoryFlagEntity flagEntity = repo.GetPCTerritoryFlagByID(flagID);
        float distance = NWScript.getDistanceBetweenLocations(flaglocation, siteLocation);

        // Scenario #1: Territory's structure cap has been reached. Blueprint not set on this construction site.
        //              Site must be razed otherwise player would go over the cap.
        if(constructionSiteID <= 0)
        {
            int structureCount = repo.GetNumberOfStructuresInTerritory(flagID);
            if(structureCount >= flagEntity.getBlueprint().getMaxStructuresCount())
            {
                return false;
            }
        }

        // Scenario #2: Construction site is a territory flag blueprint.
        // Construction site is within the flag's area of influence OR
        // the blueprint selected would bring the flag inside of its area of influence.
        return !(constructionSiteEntity != null &&
                constructionSiteEntity.getBlueprint().isTerritoryFlag() &&
                (distance <= (flagEntity.getBlueprint().getMaxBuildDistance() + constructionSiteEntity.getBlueprint().getMaxBuildDistance())));

    }


    public static boolean PlayerHasPermission(NWObject oPC, int permissionID, int flagID)
    {
        if(flagID <= 0) return true;
        if(NWScript.getIsDM(oPC)) return true;

        PlayerGO pcGO = new PlayerGO(oPC);
        StructureRepository repo = new StructureRepository();

        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(flagID);
        int buildPrivacyID = entity.getBuildPrivacy().getBuildPrivacyTypeID();

        if(buildPrivacyID == 1) // Owner Only
        {
            return pcGO.getUUID().equals(entity.getPlayerID());
        }
        else if(buildPrivacyID == 2) // Friends only
        {
            PCTerritoryFlagPermissionEntity permission = repo.GetPermissionByID(pcGO.getUUID(), permissionID, flagID);
            return permission != null || pcGO.getUUID().equals(entity.getPlayerID());
        }
        else if(buildPrivacyID == 3) // Public
        {
            return true;
        }
        else // Shouldn't reach here.
        {
            return false;
        }
    }

}
