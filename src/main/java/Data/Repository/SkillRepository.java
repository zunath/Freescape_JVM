package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCSkillEntity;
import Entities.SkillCategoryEntity;
import Entities.SkillEntity;
import Entities.SkillXPRequirementEntity;

import java.util.List;

public class SkillRepository {
    public List<SkillCategoryEntity> GetActiveCategories()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Skills/GetActiveCategories", SkillCategoryEntity.class);
        }
    }

    public List<SkillEntity> GetActiveSkillsForCategory(int skillCategoryID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Skills/GetActiveSkillsForCategory", SkillEntity.class,
                    new SqlParameter("skillCategoryID", skillCategoryID));
        }
    }

    public PCSkillEntity GetPCSkillByID(String uuid, int skillID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Skills/GetPCSkillByID", PCSkillEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("skillID", skillID));
        }
    }

    public void InsertAllPCSkillsByID(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            context.executeUpdateOrDelete("Skills/InsertAllPCSkillsByID",
                    new SqlParameter("playerID", uuid));
        }
    }

    public SkillXPRequirementEntity GetSkillXPRequirementByRank(int skillID, int rank)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Skills/GetSkillXPRequirementByRank", SkillXPRequirementEntity.class,
                    new SqlParameter("skillID", skillID),
                    new SqlParameter("rank", rank));
        }
    }

    public Integer GetSkillMaxRank(int skillID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Skills/GetSkillMaxRank",
                    new SqlParameter("skillID", skillID));
        }
    }

    public void Save(PCSkillEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
