package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCMapPinEntity;

import java.util.List;

public class MapPinRepository {

    public List<PCMapPinEntity> GetPlayerMapPins(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("MapPin/GetPlayerMapPins", PCMapPinEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public void DeletePlayerMapPins(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            context.executeUpdateOrDelete("MapPin/DeletePlayerMapPins",
                    new SqlParameter("playerID", uuid));
        }
    }

    public void Save(PCMapPinEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
