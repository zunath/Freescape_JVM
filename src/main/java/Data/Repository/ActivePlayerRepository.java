package Data.Repository;

import Data.DataContext;
import Entities.ActivePlayerEntity;
import javax.persistence.criteria.CriteriaDelete;
import java.util.List;

public class ActivePlayerRepository {

    public void Save(List<ActivePlayerEntity> entities)
    {
        try(DataContext context = new DataContext())
        {
            CriteriaDelete<ActivePlayerEntity> delete = context.getSession()
                    .getCriteriaBuilder()
                    .createCriteriaDelete(ActivePlayerEntity.class);

            delete.from(ActivePlayerEntity.class);

            context.getSession()
                    .createQuery(delete)
                    .executeUpdate();

            for(ActivePlayerEntity entity : entities)
            {
                context.getSession().saveOrUpdate(entity);
            }
        }
    }

}
