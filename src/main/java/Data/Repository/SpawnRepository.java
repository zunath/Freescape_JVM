package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.CreatureEntity;

public class SpawnRepository {

    public CreatureEntity GetCreatureByID(int creatureID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Spawn/GetCreatureByID", CreatureEntity.class,
                    new SqlParameter("creatureID", creatureID));
        }
    }

}
