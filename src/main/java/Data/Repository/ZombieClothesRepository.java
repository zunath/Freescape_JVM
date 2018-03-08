package Data.Repository;

import Data.DataContext;
import Entities.ZombieClothesEntity;

import java.util.List;

public class ZombieClothesRepository {

    public List<ZombieClothesEntity> GetAllZombieClothes()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("ZombieClothes/GetAllZombieClothes", ZombieClothesEntity.class);
        }
    }

}
