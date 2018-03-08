package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PCCorpseEntity;

import java.util.List;

public class PCCorpseRepository {

    public PCCorpseEntity GetByID(int corpseID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("PCCorpse/GetByID", PCCorpseEntity.class,
                    new SqlParameter("corpseID", corpseID));
        }
    }

    public List<PCCorpseEntity> GetAll()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLList("PCCorpse/GetAll", PCCorpseEntity.class);
        }
    }

    public void Delete(PCCorpseEntity entity)
    {
        if(entity == null) return;

        try(DataContext context = new DataContext())
        {
            context.getSession().delete(entity);
        }
    }

    public void Save(PCCorpseEntity entity)
    {
        try(DataContext context = new DataContext())
        {
            context.getSession().saveOrUpdate(entity);
        }
    }

}
