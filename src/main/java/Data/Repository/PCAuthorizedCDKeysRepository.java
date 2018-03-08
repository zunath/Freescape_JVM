package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCAuthorizedCDKeyEntity;

public class PCAuthorizedCDKeysRepository {

    public PCAuthorizedCDKeyEntity GetByAccountName(String accountName)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("PCAuthorizedCDKeys/GetByAccountName", PCAuthorizedCDKeyEntity.class,
                    new SqlParameter("accountName", accountName));
        }
    }

    public void Save(PCAuthorizedCDKeyEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
