package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCCooldownEntity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class CooldownRepository {


    public PCCooldownEntity GetPCCooldownByID(String uuid, int cooldownCategoryID)
    {
        PCCooldownEntity entity;

        try(DataContext context = new DataContext())
        {
            entity = context.executeSQLSingle("Cooldown/GetPCCooldownByID", PCCooldownEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("cooldownCategoryID", cooldownCategoryID));

            if(entity == null)
            {
                entity = new PCCooldownEntity();
                entity.setCooldownCategoryID(cooldownCategoryID);
                entity.setDateUnlocked(DateTime.now(DateTimeZone.UTC).minusSeconds(1).toDate());
                entity.setPlayerID(uuid);
            }
        }

        return entity;
    }


    public void Save(PCCooldownEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
