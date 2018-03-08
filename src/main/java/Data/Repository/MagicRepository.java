package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;
import java.util.List;

public class MagicRepository {

    public AbilityEntity GetAbilityByFeatID(int featID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Magic/GetAbilityByFeatID", AbilityEntity.class,
                    new SqlParameter("featID", featID));
        }
    }

    public AbilityEntity GetAbilityByID(int abilityID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Magic/GetAbilityByID", AbilityEntity.class,
                    new SqlParameter("abilityID", abilityID));
        }
    }

    public boolean AddAbilityToPC(String uuid, int abilityID)
    {
        boolean addedSuccessfully = false;
        AbilityEntity ability = GetAbilityByID(abilityID);

        try(DataContext context = new DataContext())
        {
            PCLearnedAbilityEntity entity = context.executeSQLSingle("Magic/GetPCLearnedAbilityByID", PCLearnedAbilityEntity.class,
                    new SqlParameter("abilityID", abilityID),
                    new SqlParameter("playerID", uuid));

            if(entity == null)
            {
                entity = new PCLearnedAbilityEntity();
                entity.setPlayerID(uuid);
                entity.setAbility(ability);
                DateTime dt = new DateTime(DateTimeZone.UTC);
                entity.setAcquiredDate(new Timestamp(dt.getMillis()));

                context.getSession().saveOrUpdate(entity);
                addedSuccessfully = true;
            }
        }

        return addedSuccessfully;
    }

    public List<AbilityCategoryEntity> GetActiveAbilityCategories()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Magic/GetActiveAbilityCategories", AbilityCategoryEntity.class);
        }
    }

    public List<PCLearnedAbilityEntity> GetPCLearnedAbilitiesByCategoryID(String uuid, int categoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Magic/GetPCLearnedAbilitiesByCategoryID", PCLearnedAbilityEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("abilityCategoryID", categoryID));
        }
    }

    public PCAbilityCooldownEntity GetPCCooldownByID(String uuid, int cooldownCategoryID)
    {
        PCAbilityCooldownEntity entity;

        try(DataContext context = new DataContext())
        {
            entity = context.executeSQLSingle("Magic/GetPCCooldownByID", PCAbilityCooldownEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("abilityCooldownCategoryID", cooldownCategoryID));

            if(entity == null)
            {
                entity = new PCAbilityCooldownEntity();
                entity.setAbilityCooldownCategoryID(cooldownCategoryID);
                entity.setDateUnlocked(DateTime.now(DateTimeZone.UTC).minusSeconds(1).toDate());
                entity.setPlayerID(uuid);
            }
        }

        return entity;
    }

    public void Save(PCAbilityCooldownEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
