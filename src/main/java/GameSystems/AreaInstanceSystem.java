package GameSystems;

import GameSystems.Models.AreaInstanceModel;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.List;

public class AreaInstanceSystem {

    private static List<AreaInstanceModel> instanceList;

    static
    {
    }

    public static void OnModuleLoad()
    {
        DuplicateAreas();
    }

    private static void DuplicateAreas()
    {
        if(instanceList == null) return;

        for(AreaInstanceModel model : instanceList)
        {
            for(int x = 1; x <= model.getInstanceCount(); x++)
            {
                NWScript.createArea(model.getAreaResref(), "", "");
            }
        }
    }
}
