package Data.Repository;

import Data.DataContext;
import Entities.BackgroundEntity;

import java.util.List;

public class BackgroundRepository {

    public List<BackgroundEntity> GetActiveBackgrounds()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Background/GetActiveBackgrounds", BackgroundEntity.class);
        }
    }
}
