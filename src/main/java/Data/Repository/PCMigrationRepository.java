package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCMigrationEntity;

public class PCMigrationRepository {

    public PCMigrationEntity GetByMigrationID(int pcMigrationID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("PCMigration/GetByMigrationID", PCMigrationEntity.class,
                    new SqlParameter("pcMigrationID", pcMigrationID));
        }
    }


}
