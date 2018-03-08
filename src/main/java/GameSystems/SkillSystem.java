package GameSystems;

import Data.Repository.SkillRepository;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class SkillSystem {

    public static void OnModuleEnter()
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(NWScript.getIsPC(oPC) && !NWScript.getIsDM(oPC) && !NWScript.getIsDMPossessed(oPC))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            SkillRepository repo = new SkillRepository();
            repo.InsertAllPCSkillsByID(pcGO.getUUID());
        }
    }

}
