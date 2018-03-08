package Data.Repository;


import Data.DataContext;
import Data.SqlParameter;
import Entities.PCOverflowItemEntity;

import java.util.List;

public class OverflowItemRepository {

    public List<PCOverflowItemEntity> GetAllByPlayerID(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("OverflowItem/GetAllByPlayerID", PCOverflowItemEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public int GetPlayerOverflowItemCount(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("OverflowItem/GetPlayerOverflowItemCount",
                    new SqlParameter("playerID", uuid));
        }
    }

    public void Save(PCOverflowItemEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void DeleteByID(int entityID)
    {
        try(DataContext context = new DataContext())
        {
            context.executeUpdateOrDelete("OverflowItem/DeleteByID",
                    new SqlParameter("pcOverflowItemID", entityID));
        }
    }
}
