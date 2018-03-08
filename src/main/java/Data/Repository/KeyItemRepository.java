package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.KeyItemEntity;
import Entities.PCKeyItemEntity;

import java.util.List;

public class KeyItemRepository {

    public List<PCKeyItemEntity> GetPlayerKeyItemsByCategory(String uuid, int categoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("KeyItem/GetPlayerKeyItemsByCategory", PCKeyItemEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("categoryID", categoryID));
        }
    }

    public KeyItemEntity GetKeyItemByID(int keyItemID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("KeyItem/GetPlayerKeyItemsByCategory", KeyItemEntity.class,
                    new SqlParameter("keyItemID", keyItemID));
        }
    }

    public PCKeyItemEntity GetPCKeyItemByKeyItemID(String uuid, int keyItemID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("KeyItem/GetPCKeyItemByKeyItemID", PCKeyItemEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("keyItemID", keyItemID));
        }
    }

    public List<Integer> GetListOfPCKeyItemIDs(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("KeyItem/GetPCKeyItemByKeyItemID",
                    new SqlParameter("playerID", uuid));
        }
    }

    public void Delete(PCKeyItemEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().delete(entity);
        }
    }

    public void Save(PCKeyItemEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
