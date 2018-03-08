package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PlayerEntity;

@SuppressWarnings("UnusedDeclaration")
public class PlayerRepository {

    public PlayerEntity GetByPlayerID(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Player/GetByPlayerID", PlayerEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public void save(PlayerEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }


}
