package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;
import java.util.List;

public class CraftRepository {

    public boolean AddBlueprintToPC(String uuid, int blueprintID)
    {
        boolean addedSuccessfully = false;

        PCBlueprintEntity entity = GetPCBlueprintByID(uuid, blueprintID);
        CraftBlueprintEntity blueprint = GetBlueprintByID(blueprintID);

        try(DataContext context = new DataContext())
        {
            if(entity == null)
            {
                entity = new PCBlueprintEntity();
                entity.setPlayerID(uuid);
                entity.setBlueprint(blueprint);
                DateTime dt = new DateTime(DateTimeZone.UTC);
                entity.setAcquiredDate(new Timestamp(dt.getMillis()));

                context.getSession().saveOrUpdate(entity);
                addedSuccessfully = true;
            }
        }

        return addedSuccessfully;
    }

    public PCBlueprintEntity GetPCBlueprintByID(String uuid, int craftBlueprintID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Craft/GetPCBlueprintByID", PCBlueprintEntity.class,
                    new SqlParameter("craftBlueprintID", craftBlueprintID),
                    new SqlParameter("playerID", uuid));
        }
    }

    public CraftBlueprintEntity GetBlueprintByID(int craftBlueprintID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Craft/GetBlueprintByID", CraftBlueprintEntity.class,
                    new SqlParameter("craftBlueprintID", craftBlueprintID));
        }
    }

    public PCCraftEntity GetPCCraftByID(String uuid, int craftID)
    {
        PCCraftEntity entity;

        try(DataContext context = new DataContext())
        {
            entity = context.executeSQLSingle("Craft/GetPCCraftByID", PCCraftEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("craftID", craftID));

            if(entity == null)
            {
                entity = new PCCraftEntity();
                entity.setExperience(0);
                entity.setLevel(1);
                entity.setPlayerID(uuid);
                entity.setCraftID(craftID);

                context.getSession().saveOrUpdate(entity);
            }

        }

        return entity;
    }

    public CraftLevelEntity GetCraftLevelByLevel(int craftID, int level)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Craft/GetCraftLevelByLevel", CraftLevelEntity.class,
                    new SqlParameter("craftID", craftID),
                    new SqlParameter("level", level));
        }
    }

    public int GetCraftMaxLevel(int craftID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Craft/GetCraftMaxLevel",
                    new SqlParameter("craftID", craftID));
        }
    }

    public CraftEntity GetCraftByID(int craftID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Craft/GetCraftByID", CraftEntity.class,
                    new SqlParameter("craftID", craftID));
        }
    }

    public List<CraftBlueprintCategoryEntity> GetCategoriesAvailableToPCByCraftID(String uuid, int craftID)
    {
        try(DataContext context = new DataContext()) {
            return context.executeSQLList("Craft/GetCraftCategoriesAvailableToPCByCraftID",
                    CraftBlueprintCategoryEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("craftID", craftID));
        }
    }

    public List<CraftBlueprintCategoryEntity> GetCategoriesAvailableToPC(String uuid)
    {
        try(DataContext context = new DataContext()) {
            return context.executeSQLList("Craft/GetCraftCategoriesAvailableToPC",
                    CraftBlueprintCategoryEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public List<PCBlueprintEntity> GetPCBlueprintsByCategoryID(String uuid, int categoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Craft/GetPCBlueprintsByCategoryID",
                    PCBlueprintEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("categoryID", categoryID));
        }
    }

    public List<PCBlueprintEntity> GetPCBlueprintsForCraftByCategoryID(String uuid, int craftID, int categoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Craft/GetPCBlueprintsForCraftByCategoryID",
                    PCBlueprintEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("categoryID", categoryID),
                    new SqlParameter("craftID", craftID));

        }
    }

    public List<CraftEntity> GetAllCrafts()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Craft/GetAllCrafts", CraftEntity.class);
        }
    }

    public void Save(Object entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
