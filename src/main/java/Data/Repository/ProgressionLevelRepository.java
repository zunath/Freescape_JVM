package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.ProgressionLevelEntity;

import java.util.List;

public class ProgressionLevelRepository {

    public List<ProgressionLevelEntity> GetAllProgressionLevels()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("ProgressionLevel/GetAllProgressionLevels", ProgressionLevelEntity.class);
        }
    }

    public ProgressionLevelEntity GetProgressionLevelByLevel(int level)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("ProgressionLevel/GetProgressionLevelByLevel", ProgressionLevelEntity.class,
                    new SqlParameter("level", level));
        }
    }

}
