package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCOutfitEntity;

@SuppressWarnings("UnusedDeclaration")
public class PCOutfitRepository {

    public PCOutfitEntity GetByUUID(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("PCOutfit/GetByUUID", PCOutfitEntity.class,
                    new SqlParameter("playerID", uuid));
        }
    }

    public void Save(PCOutfitEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
