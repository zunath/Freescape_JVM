package Data.Repository;

import Data.DataContext;
import Entities.ForcedSPResetEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ForcedSPResetRepository {

    public ForcedSPResetEntity GetLatestForcedSPResetDate()
    {
        ForcedSPResetEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<ForcedSPResetEntity> query =
                    cb.createQuery(ForcedSPResetEntity.class);

            Root<ForcedSPResetEntity> root = query.from(ForcedSPResetEntity.class);
            query.select(root)
                    .orderBy(cb.desc(root.get("dateOfReset")));

            entity = context.getSession()
                    .createQuery(query)
                    .setMaxResults(1)
                    .uniqueResult();
        }

        return entity;
    }

}
