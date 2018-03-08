package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.FameRegionEntity;
import Entities.PCRegionalFameEntity;

public class FameRepository {

    public PCRegionalFameEntity GetPCFameByID(String playerID, int regionID)
    {
        PCRegionalFameEntity entity;

        try(DataContext context = new DataContext())
        {
            entity = context.executeSQLSingle("Fame/GetPCFameByID",
                    PCRegionalFameEntity.class,
                    new SqlParameter("playerID", playerID),
                    new SqlParameter("fameRegionID", regionID));

        }

        if(entity == null)
        {
            try(DataContext context = new DataContext())
            {
                FameRegionEntity fameRegion = context.executeSQLSingle("Fame/GetFameRegionByID",
                        FameRegionEntity.class,
                        new SqlParameter("fameRegionID", regionID));

                entity = new PCRegionalFameEntity();
                entity.setAmount(0);
                entity.setPlayerID(playerID);
                entity.setFameRegion(fameRegion);
                context.getSession().saveOrUpdate(entity);
            }
        }

        return entity;
    }




    public void Save(PCRegionalFameEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }
}
