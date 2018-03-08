package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.ProgressionSkillEntity;

public class ProgressionSkillRepository {

    public ProgressionSkillEntity GetProgressionSkillByID(int skillID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("ProgressionSkill/GetProgressionSkillByID", ProgressionSkillEntity.class,
                    new SqlParameter("skillID", skillID));
        }
    }

}
