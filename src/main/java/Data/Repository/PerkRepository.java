package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

public class PerkRepository {

    public List<PerkCategoryEntity> GetPerkCategoriesForPC(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Perk/GetPerkCategoriesForPC", PerkCategoryEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public List<PerkEntity> GetPerksForPC(String uuid, int categoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Perk/GetPerksForPC", PerkEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("categoryID", categoryID));
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

    public PCPerksEntity GetPCPerkByID(String uuid, int perkID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Perk/GetPCPerkByID", PCPerksEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("perkID", perkID));
        }
    }

    public PerkLevelEntity GetPCSkillAdjustedPerkLevel(String uuid, int perkID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Perk/GetPCSkillAdjustedPerkLevel", PerkLevelEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("perkID", perkID));
        }
    }

    public Integer GetPCTotalPerkCount(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Perk/GetPCTotalPerkCount",
                    new SqlParameter("playerID", uuid));
        }
    }

    public List<PCPerkHeaderEntity> GetPCPerksForMenuHeader(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Perk/GetPCPerksForMenuHeader", "PCPerkHeaderEntityResult",
                    new SqlParameter("playerID", uuid));
        }
    }

    public void Save(PCPerksEntity pcPerk)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(pcPerk);
        }
    }

    // Old methods

    public PerkEntity GetPerkByFeatID(int featID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Perk/GetPerkByFeatID", PerkEntity.class,
                    new SqlParameter("featID", featID));
        }
    }
}
