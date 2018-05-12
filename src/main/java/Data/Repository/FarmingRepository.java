package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.GrowingPlantEntity;
import Entities.PlantEntity;

import java.util.List;

public class FarmingRepository {

    public PlantEntity GetPlantByID(int plantID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Farming/GetPlantByID", PlantEntity.class,
                    new SqlParameter("plantID", plantID));
        }
    }

    public List<PlantEntity> GetPlantsByResref(String resref)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Farming/GetPlantsByResref", PlantEntity.class,
                    new SqlParameter("resref", resref));
        }
    }

    public GrowingPlantEntity GetGrowingPlantByID(int growingPlantID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Farming/GetGrowingPlantByID", GrowingPlantEntity.class,
                    new SqlParameter("growingPlantID", growingPlantID));
        }
    }

    public List<GrowingPlantEntity> GetAllActiveGrowingPlants()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Farming/GetAllActiveGrowingPlants", GrowingPlantEntity.class);
        }
    }

    public void Save(GrowingPlantEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
