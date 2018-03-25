package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.CraftBlueprintCategoryEntity;
import Entities.CraftBlueprintEntity;

import java.util.List;

public class CraftRepository {

    public CraftBlueprintEntity GetBlueprintByID(int craftBlueprintID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Craft/GetBlueprintByID", CraftBlueprintEntity.class,
                    new SqlParameter("blueprintID", craftBlueprintID));
        }
    }

    public CraftBlueprintEntity GetBlueprintKnownByPC(String uuid, int craftBlueprintID, int deviceID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Craft/GetBlueprintKnownByPC", CraftBlueprintEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("blueprintID", craftBlueprintID),
                    new SqlParameter("deviceID", deviceID));
        }
    }

    public List<CraftBlueprintCategoryEntity> GetCategoriesAvailableToPCByDeviceID(String uuid, int deviceID)
    {
        try(DataContext context = new DataContext()) {
            return context.executeSQLList("Craft/GetCategoriesAvailableToPCByDeviceID", CraftBlueprintCategoryEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("deviceID", deviceID));
        }
    }

    public List<CraftBlueprintEntity> GetPCBlueprintsByDeviceAndCategoryID(String uuid, int deviceID, int categoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Craft/GetPCBlueprintsByDeviceAndCategoryID", CraftBlueprintEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("deviceID", deviceID),
                    new SqlParameter("categoryID", categoryID));
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
