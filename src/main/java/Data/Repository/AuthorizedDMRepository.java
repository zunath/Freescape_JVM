package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.AuthorizedDMEntity;

public class AuthorizedDMRepository {

    public AuthorizedDMEntity GetDMByCDKey(String cdKey)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("AuthorizedDM/GetDMByCDKey", AuthorizedDMEntity.class,
                    new SqlParameter("cdKey", cdKey));
        }
    }

}
