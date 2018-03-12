package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;

public class PerkRepository {

    public PerkEntity GetPerkByFeatID(int featID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Perk/GetPerkByFeatID", PerkEntity.class,
                    new SqlParameter("featID", featID));
        }
    }

    public PerkEntity GetPerkByID(int perkID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Perk/GetPerkByID", PerkEntity.class,
                    new SqlParameter("perkID", perkID));
        }
    }

    public boolean AddPerkToPC(String uuid, int perkID)
    {
        boolean addedSuccessfully = false;
        PerkEntity perk = GetPerkByID(perkID);

        try(DataContext context = new DataContext())
        {
            PCPerksEntity entity = context.executeSQLSingle("Perk/GetPCPerkByID", PCPerksEntity.class,
                    new SqlParameter("perkID", perkID),
                    new SqlParameter("playerID", uuid));

            if(entity == null)
            {
                entity = new PCPerksEntity();
                entity.setPlayerID(uuid);
                entity.setPerk(perk);
                DateTime dt = new DateTime(DateTimeZone.UTC);
                entity.setAcquiredDate(new Timestamp(dt.getMillis()));

                context.getSession().saveOrUpdate(entity);
                addedSuccessfully = true;
            }
        }

        return addedSuccessfully;
    }

    public PCCooldownEntity GetPCCooldownByID(String uuid, int cooldownCategoryID)
    {
        PCCooldownEntity entity;

        try(DataContext context = new DataContext())
        {
            entity = context.executeSQLSingle("Perk/GetPCCooldownByID", PCCooldownEntity.class,
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
