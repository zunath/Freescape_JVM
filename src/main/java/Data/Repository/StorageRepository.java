package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.StorageContainerEntity;

@SuppressWarnings("UnusedDeclaration")
public class StorageRepository {

    public StorageContainerEntity GetByContainerID(int containerID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Storage/GetByContainerID", StorageContainerEntity.class,
                    new SqlParameter("containerID", containerID));
        }
    }

    public void Save(StorageContainerEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
