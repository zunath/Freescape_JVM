package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.ItemEntity;

public class ItemRepository {

    public ItemEntity GetItemByResref(String resref)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Item/GetItemByResref", ItemEntity.class,
                    new SqlParameter("resref", resref));
        }
    }
}
