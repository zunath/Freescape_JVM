package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.*;
import org.hibernate.loader.custom.sql.SQLQueryParser;

import java.util.List;

public class StructureRepository {

    public List<ConstructionSiteEntity> GetAllConstructionSites()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetAllConstructionSites", ConstructionSiteEntity.class);
        }
    }

    public List<PCTerritoryFlagEntity> GetAllTerritoryFlags()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetAllTerritoryFlags", PCTerritoryFlagEntity.class);
        }
    }

    public PCTerritoryFlagEntity GetPCTerritoryFlagByID(int pcTerritoryFlagID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetPCTerritoryFlagByID", PCTerritoryFlagEntity.class,
                    new SqlParameter("pcTerritoryFlagID", pcTerritoryFlagID));
        }
    }

    public ConstructionSiteEntity GetConstructionSiteByID(int constructionSiteID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetConstructionSiteByID", ConstructionSiteEntity.class,
                    new SqlParameter("constructionSiteID", constructionSiteID));
        }
    }

    public PCTerritoryFlagStructureEntity GetPCStructureByID(int territoryFlagStructureID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetPCStructureByID", PCTerritoryFlagStructureEntity.class,
                    new SqlParameter("pcTerritoryFlagStructureID", territoryFlagStructureID));
        }
    }

    public StructureBlueprintEntity GetStructureBlueprintByID(int structureID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetStructureBlueprintByID", StructureBlueprintEntity.class,
                    new SqlParameter("structureBlueprintID", structureID));
        }
    }

    public List<StructureCategoryEntity> GetStructureCategories(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetStructureCategories", StructureCategoryEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public List<StructureCategoryEntity> GetStructureCategoriesByType(String uuid,
                                                                      boolean isTerritoryFlagCategory,
                                                                      boolean isVanity,
                                                                      boolean isSpecial,
                                                                      boolean isResource,
                                                                      boolean isBuilding)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetStructureCategoriesByType", StructureCategoryEntity.class,
                    new SqlParameter("isTerritoryFlagCategory", isTerritoryFlagCategory),
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("isVanity", isVanity),
                    new SqlParameter("isSpecial", isSpecial),
                    new SqlParameter("isResource", isResource),
                    new SqlParameter("isBuilding", isBuilding));
        }
    }

    public List<StructureBlueprintEntity> GetStructuresByCategoryAndType(String uuid,
                                                                         int structureCategoryID,
                                                                         boolean isVanity,
                                                                         boolean isSpecial,
                                                                         boolean isResource,
                                                                         boolean isBuilding)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetStructuresByCategoryAndType", StructureBlueprintEntity.class,
                    new SqlParameter("structureCategoryID", structureCategoryID),
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("isVanity", isVanity),
                    new SqlParameter("isSpecial", isSpecial),
                    new SqlParameter("isResource", isResource),
                    new SqlParameter("isBuilding", isBuilding));
        }
    }

    public List<StructureBlueprintEntity> GetStructuresForPCByCategory(String uuid, int structureCategoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetStructuresForPCByCategory", StructureBlueprintEntity.class,
                    new SqlParameter("structureCategoryID", structureCategoryID),
                    new SqlParameter("playerID", uuid));
        }
    }

    public List<PCTerritoryFlagPermissionEntity> GetPermissionsByFlagID(int flagID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetPermissionsByFlagID", PCTerritoryFlagPermissionEntity.class,
                    new SqlParameter("flagID", flagID));
        }
    }

    public List<PCTerritoryFlagPermissionEntity> GetPermissionsByPlayerID(String playerID, int flagID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetPermissionsByPlayerID", PCTerritoryFlagPermissionEntity.class,
                    new SqlParameter("flagID", flagID),
                    new SqlParameter("playerID", playerID));
        }
    }

    public PCTerritoryFlagPermissionEntity GetPermissionByID(String playerID, int permissionID, int flagID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetPermissionByID", PCTerritoryFlagPermissionEntity.class,
                    new SqlParameter("flagID", flagID),
                    new SqlParameter("playerID", playerID),
                    new SqlParameter("permissionID", permissionID));
        }
    }

    public List<TerritoryFlagPermissionEntity> GetAllTerritorySelectablePermissions()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetAllTerritorySelectablePermissions", TerritoryFlagPermissionEntity.class);
        }
    }

    public TerritoryFlagPermissionEntity GetTerritoryPermissionByID(int territoryPermissionID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetTerritoryPermissionByID", TerritoryFlagPermissionEntity.class,
                    new SqlParameter("permissionID", territoryPermissionID));
        }
    }

    public int GetNumberOfStructuresInTerritory(int flagID,
                                                boolean isVanity,
                                                boolean isSpecial,
                                                boolean isResource,
                                                boolean isBuilding)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetNumberOfStructuresInTerritory",
                    new SqlParameter("flagID", flagID),
                    new SqlParameter("isVanity", isVanity),
                    new SqlParameter("isSpecial", isSpecial),
                    new SqlParameter("isResource", isResource),
                    new SqlParameter("isBuilding", isBuilding));
        }
    }

    public List<PCTerritoryFlagEntity> GetAllFlagsInArea(String areaTag)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetAllFlagsInArea", PCTerritoryFlagEntity.class,
                    new SqlParameter("areaTag", areaTag));
        }
    }


    public void DeleteContainerItemByGlobalID(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            context.executeUpdateOrDelete("Structure/DeleteContainerItemByGlobalID",
                    new SqlParameter("globalID", uuid));
        }
    }

    public BuildingInteriorEntity GetDefaultBuildingInteriorByCategoryID(int buildingCategoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetDefaultBuildingInteriorByCategoryID", BuildingInteriorEntity.class,
                    new SqlParameter("buildingCategoryID", buildingCategoryID));
        }
    }

    public List<BuildingInteriorEntity> GetBuildingInteriorsByCategoryID(int buildingCategoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Structure/GetBuildingInteriorsByCategoryID", BuildingInteriorEntity.class,
                    new SqlParameter("buildingCategoryID", buildingCategoryID));
        }
    }

    public BuildingInteriorEntity GetBuildingInteriorByID(int buildingInteriorID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetBuildingInteriorByID", BuildingInteriorEntity.class,
                    new SqlParameter("buildingInteriorID", buildingInteriorID));
        }
    }

    public PCTerritoryFlagEntity GetPCTerritoryFlagByBuildingStructureID(int buildingStructureID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Structure/GetPCTerritoryFlagByBuildingStructureID", PCTerritoryFlagEntity.class,
                    new SqlParameter("buildingStructureID", buildingStructureID));
        }
    }

    public void Save(Object entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void Delete(Object entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().delete(entity);
        }
    }

}
