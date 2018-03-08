package Data.Repository;

import Data.DataContext;
import Entities.ItemCombinationEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ItemCombinationRepository {

    public ItemCombinationEntity getByItemResrefs(String itemResrefA, String itemResrefB)
    {
        ItemCombinationEntity entity;

        try(DataContext context = new DataContext())
        {
            CriteriaBuilder cb = context.getSession().getCriteriaBuilder();

            CriteriaQuery<ItemCombinationEntity> query =
                    cb.createQuery(ItemCombinationEntity.class);

            Root<ItemCombinationEntity> root = query.from(ItemCombinationEntity.class);
            query.select(root)
                    .where(
                            cb.equal(root.get("itemA"), itemResrefA),
                            cb.equal(root.get("itemB"), itemResrefB)
                    );

            entity = context.getSession()
                    .createQuery(query)
                    .uniqueResult();

            // Try the other way...
            if(entity == null)
            {
                query.select(root)
                        .where(
                                cb.equal(root.get("itemA"), itemResrefB),
                                cb.equal(root.get("itemB"), itemResrefA)
                        );

                entity = context.getSession()
                        .createQuery(query)
                        .uniqueResult();
            }
        }

        return entity;
    }

}
