package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCSearchSiteEntity;

public class SearchSiteRepository {

    public PCSearchSiteEntity GetSearchSiteByID(int searchSiteID, String playerID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("SearchSite/GetSearchSiteByID", PCSearchSiteEntity.class,
                    new SqlParameter("searchSiteID", searchSiteID),
                    new SqlParameter("playerID", playerID));
        }
    }

    public void Save(PCSearchSiteEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void Delete(PCSearchSiteEntity entity)
    {
        if(entity == null) return;

        try(DataContext context = new DataContext())
        {
            context.getSession().delete(entity);
        }
    }
}
