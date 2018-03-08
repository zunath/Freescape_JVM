package Data.Repository;

import Data.DataContext;
import Entities.ProfessionEntity;

import java.util.List;

public class ProfessionRepository {

    public List<ProfessionEntity> GetActiveProfessions()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Profession/GetActiveProfessions", ProfessionEntity.class);
        }
    }
}
