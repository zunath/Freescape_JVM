package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCQuestKillTargetProgressEntity;
import Entities.PCQuestStatusEntity;
import Entities.QuestEntity;
import Entities.QuestKillTargetListEntity;

import java.util.List;

public class QuestRepository {


    public QuestEntity GetQuestByID(int questID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Quest/GetQuestByID", QuestEntity.class,
                    new SqlParameter("questID", questID));
        }
    }

    public PCQuestStatusEntity GetPCQuestStatusByID(String playerID, int questID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Quest/GetPCQuestStatusByID", PCQuestStatusEntity.class,
                    new SqlParameter("questID", questID),
                    new SqlParameter("playerID", playerID));
        }
    }

    public List<PCQuestStatusEntity> GetAllPCQuestStatusesByID(String playerID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Quest/GetAllPCQuestStatusesByID", PCQuestStatusEntity.class,
                    new SqlParameter("playerID", playerID));
        }
    }

    public List<QuestKillTargetListEntity> GetQuestKillTargetsByQuestSequenceID(int questID, int sequenceID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Quest/GetQuestKillTargetsByQuestSequenceID", QuestKillTargetListEntity.class,
                    new SqlParameter("questID", questID),
                    new SqlParameter("sequenceID", sequenceID));
        }
    }

    public List<PCQuestKillTargetProgressEntity> GetPlayerKillTargetsByID(String playerID, int npcGroupID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Quest/GetPlayerKillTargetsByID", PCQuestKillTargetProgressEntity.class,
                    new SqlParameter("playerID", playerID),
                    new SqlParameter("npcGroupID", npcGroupID));
        }
    }

    public List<Integer> GetAllCompletedQuestIDs(String playerID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("Quest/GetAllCompletedQuestIDs",
                    new SqlParameter("playerID", playerID));
        }
    }

    public void Save(PCQuestStatusEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void Save(PCQuestKillTargetProgressEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

    public void Delete(PCQuestKillTargetProgressEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().delete(entity);
        }
    }


}
