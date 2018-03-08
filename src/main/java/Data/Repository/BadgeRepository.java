package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.BadgeEntity;
import Entities.PCBadgeEntity;

import java.util.List;

public class BadgeRepository {

    public List<PCBadgeEntity> GetPCBadgeByPlayerID(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Badge/GetPCBadgeByPlayerID", PCBadgeEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public PCBadgeEntity GetPCBadgeByBadgeID(String uuid, int badgeID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Badge/GetPCBadgeByBadgeID", PCBadgeEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("badgeID", badgeID));
        }
    }

    public BadgeEntity GetBadgeByID(int badgeID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Badge/GetBadgeByID", BadgeEntity.class,
                    new SqlParameter("badgeID", badgeID));
        }
    }

    public void Save(PCBadgeEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }


}
