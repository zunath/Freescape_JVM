package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.*;
import com.sun.tools.javac.util.Pair;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
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

    public List<SkillXPRequirementEntity> GetSkillXPRequirementsUpToRank(int skillID, int rank)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Skills/GetSkillXPRequirementsUpToRank", SkillXPRequirementEntity.class,
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

    public Integer GetPCTotalSkillCount(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Skills/GetPCTotalSkillCount",
                    new SqlParameter("playerID", uuid));
        }
    }

    public List<PCSkillEntity> GetAllPCSkills(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Skills/GetAllPCSkills", PCSkillEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public List<PCSkillEntity> GetAllUnlockedPCSkills(String uuid, int skillIDExclusion)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Skills/GetAllUnlockedPCSkills", PCSkillEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("skillID", skillIDExclusion));
        }
    }

    public List<TotalSkillXPEntity> GetTotalXPAmountsForPC(String uuid, int skillIDExclusion)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Skills/GetTotalXPAmountsForPC", "TotalSkillXPResult",
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("skillID", skillIDExclusion));
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
