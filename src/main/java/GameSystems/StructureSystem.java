package GameSystems;

import Data.Repository.PlayerRepository;
import Data.Repository.StructureRepository;
import Entities.*;
import Enumerations.StructurePermission;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ItemHelper;
import NWNX.NWNX_Chat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Duration;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.nwnx.nwnx2.jvm.NWScript.*;


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
            if(entity.isActive())
            {
                CreateConstructionSiteFromEntity(entity);
            }
        }

        List<PCTerritoryFlagEntity> territoryFlags = repo.GetAllTerritoryFlags();
        for(PCTerritoryFlagEntity flag : territoryFlags)
        {
            NWObject oArea = getObjectByTag(flag.getLocationAreaTag(), 0);
            NWVector position = vector((float) flag.getLocationX(), (float) flag.getLocationY(), (float) flag.getLocationZ());
            NWLocation location = location(oArea, position, (float) flag.getLocationOrientation());
            PlayerEntity playerEntity = playerRepo.GetByPlayerID(flag.getPlayerID());

            NWObject territoryFlag = createObject(ObjectType.PLACEABLE, flag.getBlueprint().getResref(), location, false, "");
            setLocalInt(territoryFlag, TerritoryFlagIDVariableName, flag.getPcTerritoryFlagID());

            if(flag.showOwnerName())
            {
                setName(territoryFlag, playerEntity.getCharacterName() + "'s Territory");
            }
            else
            {
                setName(territoryFlag, "Claimed Territory");
            }

            for(PCTerritoryFlagStructureEntity structure : flag.getStructures())
            {
                if(structure.isActive())
                {
                    CreateStructureFromEntity(structure);
                }
            }

        }
    }

    public static void CreateConstructionSiteFromEntity(ConstructionSiteEntity entity)
    {
        NWObject oArea = getObjectByTag(entity.getLocationAreaTag(), 0);
        NWVector position = vector((float) entity.getLocationX(), (float) entity.getLocationY(), (float) entity.getLocationZ());
        NWLocation location = location(oArea, position, (float) entity.getLocationOrientation());

        NWObject constructionSite = createObject(ObjectType.PLACEABLE, ConstructionSiteResref, location, false, "");
        setLocalInt(constructionSite, ConstructionSiteIDVariableName, entity.getConstructionSiteID());
        setName(constructionSite, "Construction Site: " + entity.getBlueprint().getName());

        NWEffect eGhostWalk = effectCutsceneGhost();
        applyEffectToObject(Duration.TYPE_PERMANENT, eGhostWalk, constructionSite, 0.0f);
    }

    public static void CreateStructureFromEntity(PCTerritoryFlagStructureEntity structure)
    {
        NWObject oArea = getObjectByTag(structure.getLocationAreaTag(), 0);
        NWVector position = vector((float) structure.getLocationX(), (float) structure.getLocationY(), (float) structure.getLocationZ());
        NWLocation location = location(oArea, position, (float) structure.getLocationOrientation());

        NWObject structurePlaceable = createObject(ObjectType.PLACEABLE, structure.getBlueprint().getResref(), location, false, "");
        setLocalInt(structurePlaceable, StructureIDVariableName, structure.getPcTerritoryFlagStructureID());
        setPlotFlag(structurePlaceable, true);
        setUseableFlag(structurePlaceable, structure.isUseable());

        if(!structure.getCustomName().equals(""))
        {
            setName(structurePlaceable, structure.getCustomName());
        }
        else if(!structure.getBlueprint().getResourceResref().equals(""))
        {
            setName(structurePlaceable, structure.getBlueprint().getName());
        }

        if(structure.getBlueprint().getItemStorageCount() > 0)
        {
            setName(structurePlaceable, getName(structurePlaceable, false) + " (" + structure.getBlueprint().getItemStorageCount() + " items)");
        }

        if(structure.getBlueprint().isBuilding())
        {
            NWObject door = CreateBuildingDoor(location, structure.getPcTerritoryFlagStructureID());
            setLocalObject(structurePlaceable, "BUILDING_ENTRANCE_DOOR", door);
        }
    }

    public static void OnModuleNWNXChat(NWObject sender)
    {
        if(getLocalInt(sender, "LISTENING_FOR_NEW_CONTAINER_NAME") != 1) return;
        if(!getIsPC(sender)) return;

        NWNX_Chat.SkipMessage();
        String text = NWNX_Chat.GetMessage().trim();
        if(text.length() > 32)
        {
            floatingTextStringOnCreature("Container names must be 32 characters or less.", sender, false);
            return;
        }

        setLocalString(sender, "NEW_CONTAINER_NAME", text);
        sendMessageToPC(sender, "New container name received. Please press the 'Next' button in the conversation window.");
    }

    public static boolean IsPCMovingStructure(NWObject oPC)
    {
        return getLocalInt(oPC, IsMovingStructureLocationVariableName) == 1 &&
                !getLocalObject(oPC, MovingStructureVariableName).equals(NWObject.INVALID);
    }

    public static void SetIsPCMovingStructure(NWObject oPC, NWObject structure, boolean isMoving)
    {
        if(isMoving)
        {
            setLocalInt(oPC, IsMovingStructureLocationVariableName, 1);
            setLocalObject(oPC, MovingStructureVariableName, structure);
        }
        else
        {
            deleteLocalInt(oPC, IsMovingStructureLocationVariableName);
            deleteLocalObject(oPC, MovingStructureVariableName);
        }
    }

    private static void SetPlaceableStructureID(NWObject structure, int structureID)
    {
        setLocalInt(structure, StructureIDVariableName, structureID);
    }

    public static int GetPlaceableStructureID(NWObject structure)
    {
        return getLocalInt(structure, StructureIDVariableName) <= 0 ?
                -1 :
                getLocalInt(structure, StructureIDVariableName);
    }

    // Assumption: There will never be an overlap of two or more territory flags' areas of influence.
    // If no territory flags own the location, NWObject.INVALID is returned.
    // If area is a building interior, the area itself will be treated as a flag.
    public static NWObject GetTerritoryFlagOwnerOfLocation(NWLocation location)
    {
        if(GetTerritoryFlagID(location.getArea()) > 0) return location.getArea();

        StructureRepository repo = new StructureRepository();
        String areaTag = getTag(location.getArea());
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
            float distance = getDistanceBetweenLocations(flagLocation, location);

            if(distance <= flag.getBlueprint().getMaxBuildDistance())
            {
                // Found the territory which "owns" this location. Look for the flag placeable with a matching ID.
                int currentPlaceable = 1;
                NWObject checker = createObject(ObjectType.PLACEABLE, TemporaryLocationCheckerObjectResref, location, false, "");

                do
                {
                    placeable = getNearestObject(ObjectType.PLACEABLE, checker, currentPlaceable);

                    if(GetTerritoryFlagID(placeable) == flag.getPcTerritoryFlagID())
                    {
                        break;
                    }

                    currentPlaceable++;
                } while(!placeable.equals(NWObject.INVALID));


                destroyObject(checker, 0.0f);

            }

        }
        return placeable;
    }

    public static int CanPCBuildInLocation(NWObject oPC, NWLocation targetLocation, int permissionCheck)
    {
        if(getLocalInt(targetLocation.getArea(), "BUILDING_DISABLED") == 1) return 0;

        StructureRepository repo = new StructureRepository();
        NWObject flag = GetTerritoryFlagOwnerOfLocation(targetLocation);
        NWLocation flagLocation = getLocation(flag);
        int pcTerritoryFlagID = GetTerritoryFlagID(flag);
        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(pcTerritoryFlagID);
        PlayerGO pcGO = new PlayerGO(oPC);
        float distance = getDistanceBetweenLocations(flagLocation, targetLocation);

        // No territory flag found, or the distance is too far from the nearest territory flag.
        // Only for non-building areas.
        if((flag.equals(NWObject.INVALID) ||
                distance > entity.getBlueprint().getMaxBuildDistance()))
        {
            return 1;
        }


        int vanityCount = repo.GetNumberOfStructuresInTerritory(pcTerritoryFlagID, true, false, false, false);
        int specialCount = repo.GetNumberOfStructuresInTerritory(pcTerritoryFlagID, false, true, false, false);
        int resourceCount = repo.GetNumberOfStructuresInTerritory(pcTerritoryFlagID, false, false, true, false);
        int buildingCount = repo.GetNumberOfStructuresInTerritory(pcTerritoryFlagID, false, false, false, true);
        if(vanityCount >= entity.getBlueprint().getVanityCount() &&
                specialCount >= entity.getBlueprint().getSpecialCount() &&
                resourceCount >= entity.getBlueprint().getResourceCount() &&
                buildingCount >= entity.getBlueprint().getBuildingCount())
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
        NWLocation location = getLocation(oCheck);
        NWObject flag = GetTerritoryFlagOwnerOfLocation(location);

        if(flag.equals(location.getArea())) return true;
        if(flag.equals(NWObject.INVALID)) return false;

        float distance = getDistanceBetween(oCheck, flag);
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
            floatingTextStringOnCreature(ColorToken.Red() + "You can't build a construction site there." + ColorToken.End(), oPC, false);
        }
        else if(buildStatus == 1) // 1 = Success
        {
            NWObject constructionSite = createObject(ObjectType.PLACEABLE, ConstructionSiteResref, location, false, "");
            NWEffect eGhostWalk = effectCutsceneGhost();
            applyEffectToObject(Duration.TYPE_PERMANENT, eGhostWalk, constructionSite, 0.0f);

            floatingTextStringOnCreature("Construction site created! Use the construction site to select a blueprint.", oPC, false);
        }
        else if(buildStatus == 2) // 2 = Territory can't hold any more structures.
        {
            floatingTextStringOnCreature(ColorToken.Red() + "The maximum number of structures this territory can manage has been reached. Raze a structure or upgrade your territory to create a new structure." + ColorToken.End(), oPC, false);
        }
    }

    public static int GetConstructionSiteID(NWObject site)
    {
        return getLocalInt(site, ConstructionSiteIDVariableName);
    }

    private static void SetConstructionSiteID(NWObject site, int constructionSiteID)
    {
        setLocalInt(site, ConstructionSiteIDVariableName, constructionSiteID);
    }

    public static int GetTerritoryFlagID(NWObject flag)
    {
        return getLocalInt(flag, TerritoryFlagIDVariableName);
    }

    private static float GetAdjustedFacing(float facing)
    {
        while(facing > 360.0f)
        {
            facing = facing - 360.0f;
        }

        return  facing;
    }

    private static float GetAdjustedFacing(NWLocation location)
    {
        return GetAdjustedFacing(location.getFacing());
    }

    public static void SelectBlueprint(NWObject oPC, NWObject constructionSite, int blueprintID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        StructureRepository repo = new StructureRepository();
        ConstructionSiteEntity entity = new ConstructionSiteEntity();
        NWObject area = getArea(constructionSite);
        String areaTag = getTag(area);
        NWLocation location = getLocation(constructionSite);
        StructureBlueprintEntity blueprint = repo.GetStructureBlueprintByID(blueprintID);

        entity.setLocationAreaTag(areaTag);
        entity.setLocationOrientation(GetAdjustedFacing(location));
        entity.setLocationX(location.getX());
        entity.setLocationY(location.getY());
        entity.setLocationZ(location.getZ());
        entity.setPlayerID(pcGO.getUUID());
        entity.setBlueprint(blueprint);
        entity.setComponents(new ArrayList<>());
        entity.setActive(true);

        for(StructureComponentEntity comp: blueprint.getComponents())
        {
            ConstructionSiteComponentEntity csComp = new ConstructionSiteComponentEntity();
            csComp.setConstructionSite(entity);
            csComp.setQuantity(comp.getQuantity());
            csComp.setStructureComponent(comp);
            entity.getComponents().add(csComp);
        }

        if(IsWithinRangeOfTerritoryFlag(constructionSite))
        {
            NWObject flag = GetTerritoryFlagOwnerOfLocation(location);
            PCTerritoryFlagEntity flagEntity = repo.GetPCTerritoryFlagByID(GetTerritoryFlagID(flag));
            entity.setPcTerritoryFlag(flagEntity);
        }

        // Buildings - get the default interior and assign it to the construction site.
        if(blueprint.isBuilding())
        {
            if(blueprint.getBuildingCategory() == null)
            {
                floatingTextStringOnCreature("ERROR: Unable to locate building category for this blueprint. Please inform an admin.", oPC, false);
                return;
            }

            BuildingInteriorEntity defaultInterior = repo.GetDefaultBuildingInteriorByCategoryID(blueprint.getBuildingCategory().getBuildingCategoryID());
            entity.setBuildingInterior(defaultInterior);
        }

        repo.Save(entity);
        SetConstructionSiteID(constructionSite, entity.getConstructionSiteID());
        setName(constructionSite, "Construction Site: " + entity.getBlueprint().getName());

        // If blueprint doesn't have any components, instantly create the structure
        if(blueprint.getComponents().isEmpty())
        {
            StructureSystem.CompleteStructure(constructionSite);
        }
        else
        {
            floatingTextStringOnCreature("Blueprint set. Equip a hammer and 'bash' the construction site to build.", oPC, false);
        }
    }

    public static void MoveStructure(NWObject oPC, NWLocation location)
    {
        StructureRepository repo = new StructureRepository();
        NWObject target = getLocalObject(oPC, MovingStructureVariableName);
        NWObject nearestFlag = GetTerritoryFlagOwnerOfLocation(location);
        NWLocation nearestFlagLocation = getLocation(nearestFlag);
        int nearestFlagID = GetTerritoryFlagID(nearestFlag);
        boolean outsideOwnFlagRadius = false;
        int constructionSiteID = GetConstructionSiteID(target);
        int structureID = GetPlaceableStructureID(target);
        deleteLocalInt(oPC, IsMovingStructureLocationVariableName);
        deleteLocalObject(oPC, MovingStructureVariableName);

        if(target.equals(NWObject.INVALID) ||
                !location.getArea().equals(getArea(target))) {
            return;
        }

        if(!PlayerHasPermission(oPC, StructurePermission.CanMoveStructures, nearestFlagID))
        {
            floatingTextStringOnCreature("You do not have permission to move this structure.", oPC, false);
            return;
        }

        // Moving construction site, no blueprint set
        if(constructionSiteID <= 0 && getResRef(target).equals(ConstructionSiteResref))
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
                if(nearestFlagEntity != null && getDistanceBetweenLocations(location, nearestFlagLocation) <= nearestFlagEntity.getBlueprint().getMaxBuildDistance())
                {
                    floatingTextStringOnCreature("Cannot move territory markers within the building range of another territory marker.", oPC, false);
                    return;
                }
            }
            else if(entity.getPcTerritoryFlag().getPcTerritoryFlagID() != nearestFlagID ||
                    getDistanceBetweenLocations(nearestFlagLocation, location) > entity.getPcTerritoryFlag().getBlueprint().getMaxBuildDistance())
            {
                outsideOwnFlagRadius = true;
            }
            else
            {
                entity.setLocationOrientation(GetAdjustedFacing(location));
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
                    getDistanceBetweenLocations(nearestFlagLocation, location) > entity.getPcTerritoryFlag().getBlueprint().getMaxBuildDistance())
            {
                outsideOwnFlagRadius = true;
            }
            else
            {
                entity.setLocationOrientation(GetAdjustedFacing(location));
                entity.setLocationX(location.getX());
                entity.setLocationY(location.getY());
                entity.setLocationZ(location.getZ());

                repo.Save(entity);
            }
        }

        if(outsideOwnFlagRadius)
        {
            floatingTextStringOnCreature("Unable to move structure to that location. New location must be within range of the territory marker it is attached to.", oPC, false);
            return;
        }


        NWObject door = getLocalObject(target, "BUILDING_ENTRANCE_DOOR");
        boolean hasDoor = getIsObjectValid(door);

        NWObject copy = createObject(ObjectType.PLACEABLE, getResRef(target), location, false, "");
        setName(copy, getName(target, false));

        if(hasDoor)
        {
            destroyObject(door, 0.0f);
            door = CreateBuildingDoor(getLocation(copy), structureID);
            setLocalObject(copy, "BUILDING_ENTRANCE_DOOR", door);
        }

        if(constructionSiteID > 0) SetConstructionSiteID(copy, constructionSiteID);
        else if (structureID > 0) SetPlaceableStructureID(copy, structureID);

        destroyObject(getLocalObject(target, "GateBlock"), 0.0f);
        destroyObject(target, 0.0f);
    }

    public static void LogQuickBuildAction(NWObject oDM, NWObject completedStructure)
    {
        if(!getIsPC(oDM) && !getIsDM(oDM)) return;

        StructureRepository repo = new StructureRepository();
        String name = getName(oDM, false);
        int flagID = getLocalInt(completedStructure, TerritoryFlagIDVariableName);
        long structureID = (long)getLocalInt(completedStructure, StructureIDVariableName);

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
        NWLocation location = getLocation(constructionSite);

        NWObject structurePlaceable = createObject(ObjectType.PLACEABLE, blueprint.getResref(), location, false, "");
        destroyObject(constructionSite, 0.0f);

        if(blueprint.isTerritoryFlag())
        {
            PlayerRepository playerRepo = new PlayerRepository();
            PlayerEntity playerEntity = playerRepo.GetByPlayerID(entity.getPlayerID());
            setName(structurePlaceable, playerEntity.getCharacterName() + "'s Territory");

            PCTerritoryFlagEntity pcFlag = new PCTerritoryFlagEntity();
            pcFlag.setBlueprint(blueprint);
            pcFlag.setLocationAreaTag(entity.getLocationAreaTag());
            pcFlag.setLocationOrientation(entity.getLocationOrientation());
            pcFlag.setLocationX(entity.getLocationX());
            pcFlag.setLocationY(entity.getLocationY());
            pcFlag.setLocationZ(entity.getLocationZ());
            pcFlag.setPlayerID(entity.getPlayerID());
            pcFlag.setShowOwnerName(true);
            pcFlag.setBuildingPCStructureID(null);
            pcFlag.setActive(true);

            repo.Save(pcFlag);
            setLocalInt(structurePlaceable, TerritoryFlagIDVariableName, pcFlag.getPcTerritoryFlagID());
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
            pcStructure.setCustomName("");
            pcStructure.setBuildingInterior(entity.getBuildingInterior());

            repo.Save(pcStructure);
            setLocalInt(structurePlaceable, StructureIDVariableName, pcStructure.getPcTerritoryFlagStructureID());

            if(entity.getBlueprint().getItemStorageCount() > 0)
            {
                if(entity.getBlueprint().getResourceResref().equals(""))
                {
                    setName(structurePlaceable, getName(structurePlaceable, false) + " (" + entity.getBlueprint().getItemStorageCount() + " items)");
                }
                else
                {
                    setName(structurePlaceable, entity.getBlueprint().getName() + " (" + entity.getBlueprint().getItemStorageCount() + " items)");
                }
            }

            if(entity.getBlueprint().isBuilding())
            {
                // Buildings get an entry in the territory flags table. There's no physical territory marker in-game, it's all done in the DB.
                PCTerritoryFlagEntity pcFlag = new PCTerritoryFlagEntity();
                pcFlag.setBlueprint(blueprint);
                pcFlag.setLocationAreaTag("");
                pcFlag.setLocationOrientation(0.0f);
                pcFlag.setLocationX(0.0f);
                pcFlag.setLocationY(0.0f);
                pcFlag.setLocationZ(0.0f);
                pcFlag.setPlayerID(entity.getPlayerID());
                pcFlag.setShowOwnerName(false);
                pcFlag.setBuildingPCStructureID(pcStructure.getPcTerritoryFlagStructureID());
                pcFlag.setActive(true);

                repo.Save(pcFlag);
                CreateBuildingDoor(location, pcStructure.getPcTerritoryFlagStructureID());
            }
        }

        entity.setActive(false);
        repo.Save(entity);

        return structurePlaceable;
    }

    public static NWObject CreateBuildingDoor(NWLocation houseLocation, int structureID)
    {
        float facing = GetAdjustedFacing(houseLocation.getFacing() + 146.31f);
        float x = houseLocation.getX();
        float y = houseLocation.getY();
        float z = houseLocation.getZ();

        // Adjust X and Y positions
        float mod = (float)(Math.sqrt(13.0f) * Math.sin(facing));
        x = x + mod;

        mod = (float)(Math.sqrt(13.0f) * Math.cos(facing));
        y = y - mod;

        NWVector position = vector(x, y, z);
        NWLocation doorLocation = location(houseLocation.getArea(), position, facing);
        NWObject door = createObject(ObjectType.PLACEABLE, "building_door", doorLocation, false, "");
        setLocalInt(door, StructureIDVariableName, structureID);
        setLocalInt(door, "IS_BUILDING_DOOR", 1);

        return door;
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
        NWObject placeable = getNearestObject(ObjectType.PLACEABLE, flag, currentPlaceable);
        while(!placeable.equals(NWObject.INVALID))
        {
            if(getDistanceBetween(placeable, flag) > entity.getBlueprint().getMaxBuildDistance()) break;

            if(constructionSiteIDs.contains(GetConstructionSiteID(placeable)) ||
                    structureSiteIDs.contains(GetPlaceableStructureID(placeable)))
            {

                destroyObject(getLocalObject(placeable, "GateBlock"), 0.0f);
                destroyObject(placeable, 0.0f);
            }

            currentPlaceable++;
            placeable = getNearestObject(ObjectType.PLACEABLE, flag, currentPlaceable);
        }

        destroyObject(flag, 0.0f);

        repo.SetTerritoryInactive(flagID);
    }

    public static void TransferTerritoryOwnership(NWObject oFlag, String newOwnerUUID)
    {
        PlayerRepository playerRepo = new PlayerRepository();
        StructureRepository repo = new StructureRepository();
        int pcFlagID = GetTerritoryFlagID(oFlag);
        PlayerEntity playerEntity = playerRepo.GetByPlayerID(newOwnerUUID);

        // Update building territory marker owner
        repo.UpdateBuildingTerritoryFlagsOwner(newOwnerUUID, pcFlagID);

        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(pcFlagID);
        entity.getPermissions().clear();
        entity.setPlayerID(newOwnerUUID);
        entity.setShowOwnerName(true);
        repo.Save(entity);

        setName(oFlag, playerEntity.getCharacterName() + "'s Territory");

        for(NWObject oPC : getPCs())
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            if(pcGO.getUUID().equals(newOwnerUUID))
            {
                floatingTextStringOnCreature("Ownership of a territory in " + getName(getArea(oFlag), false)  + " has been transferred to you.", oPC, false);
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
                for(ConstructionSiteComponentEntity comp: entity.getComponents())
                {
                    int quantity = comp.getStructureComponent().getQuantity() - comp.getQuantity();
                    for(int q = 1; q <= quantity; q++) createItemOnObject(comp.getStructureComponent().getResref(), oPC, 1, "");
                }
            }

            entity.setActive(false);
            repo.Save(entity);
        }
        destroyObject(site, 0.0f);
    }

    public static boolean WillBlueprintOverlapWithExistingFlags(NWLocation location, int blueprintID)
    {
        StructureRepository repo = new StructureRepository();
        NWObject area = getAreaFromLocation(location);
        List<PCTerritoryFlagEntity> flags = repo.GetAllFlagsInArea(getTag(area));
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

            float distance = getDistanceBetweenLocations(location, flagLocation);
            float overlapDistance = (float)flag.getBlueprint().getMaxBuildDistance() + (float)blueprint.getMaxBuildDistance();

            if(distance <= overlapDistance)
                return true;

        }

        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean IsConstructionSiteValid(NWObject site)
    {
        StructureRepository repo = new StructureRepository();
        NWLocation siteLocation = getLocation(site);
        NWObject flag = GetTerritoryFlagOwnerOfLocation(siteLocation);
        NWLocation flaglocation = getLocation(flag);
        int flagID = StructureSystem.GetTerritoryFlagID(flag);
        int constructionSiteID = StructureSystem.GetConstructionSiteID(site);
        if(flagID <= 0) return true;

        ConstructionSiteEntity constructionSiteEntity = repo.GetConstructionSiteByID(constructionSiteID);
        PCTerritoryFlagEntity flagEntity = repo.GetPCTerritoryFlagByID(flagID);
        float distance = getDistanceBetweenLocations(flaglocation, siteLocation);

        // Scenario #1: Territory's structure cap has been reached. Blueprint not set on this construction site.
        //              Site must be razed otherwise player would go over the cap.
        if(constructionSiteID <= 0)
        {
            int vanityCount = repo.GetNumberOfStructuresInTerritory(flagID, true, false, false, false);
            int specialCount = repo.GetNumberOfStructuresInTerritory(flagID, false, true, false, false);
            int resourceCount = repo.GetNumberOfStructuresInTerritory(flagID, false, false, true, false);
            int buildingCount = repo.GetNumberOfStructuresInTerritory(flagID, false, false, false, true);
            if(vanityCount >= flagEntity.getBlueprint().getVanityCount() &&
                    specialCount >= flagEntity.getBlueprint().getSpecialCount() &&
                    resourceCount >= flagEntity.getBlueprint().getResourceCount() &&
                    buildingCount >= flagEntity.getBlueprint().getBuildingCount())
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
        if(getIsDM(oPC)) return true;

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
        else //noinspection RedundantIfStatement
            if(buildPrivacyID == 3) // Public
        {
            return true;
        }
        else // Shouldn't reach here.
        {
            return false;
        }
    }

    public static String BuildMenuHeader(int blueprintID)
    {
        StructureRepository repo = new StructureRepository();
        StructureBlueprintEntity entity = repo.GetStructureBlueprintByID(blueprintID);
        String header = ColorToken.Green() + "Blueprint Name: " + ColorToken.End() + entity.getName() + "\n";
        header += ColorToken.Green() + "Level: " + ColorToken.End() + entity.getLevel() + "\n\n";

        if(entity.getMaxBuildDistance() > 0.0f)
        {
            header += ColorToken.Green() + "Build Distance: " + ColorToken.End() + entity.getMaxBuildDistance() + " meters" + "\n";
        }
        if(entity.getVanityCount() > 0)
        {
            header += ColorToken.Green() + "Max # of Vanity Structures: " + ColorToken.End() + entity.getVanityCount() + "\n";
        }
        if(entity.getSpecialCount() > 0)
        {
            header += ColorToken.Green() + "Max # of Special Structures: " + ColorToken.End() + entity.getSpecialCount() + "\n";
        }
        if(entity.getItemStorageCount() > 0)
        {
            header += ColorToken.Green() + "Item Storage: " + ColorToken.End() + entity.getItemStorageCount() + " items" + "\n";
        }

        if(!entity.getDescription().equals(""))
        {
            header += ColorToken.Green() + "Description: " + ColorToken.End() + entity.getDescription() + "\n\n";
        }
        header += (ColorToken.Green() + "Resources Required: " + ColorToken.End() + "\n") + "\n";


        for(StructureComponentEntity comp: entity.getComponents())
        {
            //noinspection StringConcatenationInLoop
            header += comp.getQuantity() > 0 ? comp.getQuantity() + "x " + ItemHelper.GetNameByResref(comp.getResref()) + "\n" : "";
        }

        return header;
    }

    public static void SetStructureCustomName(NWObject oPC, NWObject structure, String customName)
    {
        StructureRepository repo = new StructureRepository();
        int structureID = GetPlaceableStructureID(structure);
        customName = customName.trim();

        if(structureID <= 0) return;
        if(customName.equals("")) return;

        PCTerritoryFlagStructureEntity entity = repo.GetPCStructureByID(structureID);

        if(!PlayerHasPermission(oPC, StructurePermission.CanRenameStructures, entity.getPcTerritoryFlag().getPcTerritoryFlagID()))
        {
            floatingTextStringOnCreature("You don't have permission to rename structures. Contact the territory owner for permission.", oPC, false);
            return;
        }

        entity.setCustomName(customName);

        repo.Save(entity);
        setName(structure, customName);

        floatingTextStringOnCreature("New name set: " + customName, oPC, false);
    }

    public static void PreviewBuildingInterior(NWObject oPC, int buildingInteriorID)
    {
        StructureRepository repo = new StructureRepository();
        BuildingInteriorEntity interior = repo.GetBuildingInteriorByID(buildingInteriorID);

        NWObject area = createArea(interior.getAreaResref(), "", "PREVIEW - " + interior.getName());
        setLocalInt(area, "BUILDING_DISABLED", 1);
        JumpPCToBuildingInterior(oPC, area);
    }

    public static void JumpPCToBuildingInterior(NWObject oPC, NWObject area)
    {
        NWObject[] objects = getObjectsInArea(area);

        NWObject waypoint = null;
        NWObject exit = null;

        for(NWObject obj: objects)
        {
            String tag = getTag(obj);
            if(tag.equals("PLAYER_HOME_ENTRANCE"))
            {
                waypoint = obj;
            }
            else if(tag.equals("building_exit"))
            {
                exit = obj;
            }
        }

        if(waypoint == null)
        {
            floatingTextStringOnCreature("ERROR: Couldn't find the building interior's entrance. Inform an admin of this issue.", oPC, false);
            return;
        }

        if(exit == null)
        {
            floatingTextStringOnCreature("ERROR: Couldn't find the building interior's exit. Inform an admin of this issue.", oPC, false);
            return;
        }

        LocationSystem.SaveLocation(oPC, getArea(oPC));

        setLocalLocation(exit, "PLAYER_HOME_EXIT_LOCATION", getLocation(oPC));

        NWLocation location = getLocation(waypoint);
        Scheduler.assignNow(oPC, () -> actionJumpToLocation(location));
    }

}
