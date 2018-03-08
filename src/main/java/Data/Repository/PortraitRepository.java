package Data.Repository;

import Data.DataContext;
import Data.SqlParameter;
import Entities.PortraitEntity;

public class PortraitRepository {

    public PortraitEntity GetByPortraitID(int portraitID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Portrait/GetByPortraitID", PortraitEntity.class,
                    new SqlParameter("portraitID", portraitID));
        }
    }

    public PortraitEntity GetBy2DAID(int _2daID)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Portrait/GetBy2DAID", PortraitEntity.class,
                    new SqlParameter("_2DAID", _2daID));
        }
    }

    public PortraitEntity GetByResref(String portraitResref)
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Portrait/GetByResref", PortraitEntity.class,
                    new SqlParameter("resref", portraitResref));
        }
    }
    public int GetNumberOfPortraits()
    {
        try(DataContext context = new DataContext())
        {
            return context.executeSQLSingle("Portrait/GetNumberOfPortraits");
        }
    }


}
