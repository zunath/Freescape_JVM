package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PlayerProgressionSkillEntity;

public class PlayerProgressionSkillsRepository {

    public PlayerProgressionSkillEntity GetByPlayerIDAndSkillID(String uuid, int skillID)
    {
        PlayerProgressionSkillEntity entity;

        try(DataContext context = new DataContext())
        {
            entity = context.executeSQLSingle("PlayerProgressionSkills/GetByPlayerIDAndSkillID", PlayerProgressionSkillEntity.class,
                    new SqlParameter("playerID", uuid),
                    new SqlParameter("progressionSkillID", skillID));

            if(entity == null)
            {
                entity = new PlayerProgressionSkillEntity();
                entity.setPcID(uuid);
                entity.setProgressionSkillID(skillID);
            }
        }

        return entity;
    }

    public void DeleteAllByPlayerID(String uuid)
    {
        try(DataContext context = new DataContext())
        {
            context.executeUpdateOrDelete("PlayerProgressionSkills/DeleteAllByPlayerID",
                    new SqlParameter("playerID", uuid));
        }
    }

    public void save(PlayerProgressionSkillEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
