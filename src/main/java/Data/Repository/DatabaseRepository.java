package Data.Repository;

import Data.DataContext;

public class DatabaseRepository {

    public void KeepAlive()
    {
        try(DataContext context = new DataContext())
        {
            context.executeSQLSingle("Database/KeepAlive");
        }
    }
}
