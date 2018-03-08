package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.CustomEffectEntity;
import Entities.PCCustomEffectEntity;

import java.util.List;

public class CustomEffectRepository {

    public List<PCCustomEffectEntity> GetPCEffects(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("CustomEffect/GetPCEffects", PCCustomEffectEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public PCCustomEffectEntity GetPCEffectByID(String uuid, int customEffectID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("CustomEffect/GetPCEffectByID", PCCustomEffectEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("customEffectID", customEffectID));
        }
    }

    public CustomEffectEntity GetEffectByID(int customEffectID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("CustomEffect/GetEffectByID", CustomEffectEntity.class,
                    new SqlParameter("customEffectID", customEffectID));
        }
    }

    public void Save(Object entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void Delete(Object entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().delete(entity);
        }
    }


}
