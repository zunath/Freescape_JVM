package Data.Repository;

import Data.DataContext;
import Entities.ServerConfigurationEntity;

public class ServerConfigurationRepository {

    public static ServerConfigurationEntity GetServerConfiguration()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("ServerConfiguration/GetServerConfiguration", ServerConfigurationEntity.class);
        }
    }

}
