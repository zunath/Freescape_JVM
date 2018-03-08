package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.*;

import java.util.List;

public class ResearchRepository {

    public List<PCTerritoryFlagsStructuresResearchQueueEntity> GetResearchJobsInQueue(int pcStructureID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Research/GetResearchJobsInQueue", PCTerritoryFlagsStructuresResearchQueueEntity.class,
                    new SqlParameter("pcStructureID", pcStructureID));
        }
    }

    public PCTerritoryFlagsStructuresResearchQueueEntity GetResearchJobInQueueForSlot(int pcStructureID, int researchSlotID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Research/GetResearchJobInQueueForSlot", PCTerritoryFlagsStructuresResearchQueueEntity.class,
                    new SqlParameter("pcStructureID", pcStructureID),
                    new SqlParameter("researchSlotID", researchSlotID));
        }
    }

    public List<CraftBlueprintCategoryEntity> GetResearchCategoriesAvailableForCraftAndLevel(int craftID, int researchLevel)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Research/GetResearchCategoriesAvailableForCraftAndLevel", CraftBlueprintCategoryEntity.class,
                    new SqlParameter("craftID", craftID),
                    new SqlParameter("skillRequired", researchLevel));
        }
    }

    public List<ResearchBlueprintEntity> GetResearchBlueprintsForCategory(int craftID, int categoryID, int researchLevel)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Research/GetResearchBlueprintsForCategory", ResearchBlueprintEntity.class,
                    new SqlParameter("craftID", craftID),
                    new SqlParameter("skillRequired", researchLevel),
                    new SqlParameter("craftBlueprintCategoryID", categoryID));
        }
    }

    public ResearchBlueprintEntity GetResearchBlueprintByID(int researchBlueprintID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Research/GetResearchBlueprintByID", ResearchBlueprintEntity.class,
                    new SqlParameter("researchBlueprintID", researchBlueprintID));
        }
    }

    public void Save(PCTerritoryFlagsStructuresResearchQueueEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void DeleteAllByStructureID(int pcStructureID)
    {
        try(DataContext context = new DataContext())
        {
            context.executeUpdateOrDelete("Research/DeleteAllByStructureID",
                    new SqlParameter("pcStructureID", pcStructureID));
        }
    }
}
