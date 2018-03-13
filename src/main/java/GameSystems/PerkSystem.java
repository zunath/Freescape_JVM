package GameSystems;

import Data.Repository.PerkRepository;
import Entities.PCPerksEntity;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class PerkSystem {

    public static int GetPCPerkLevel(NWObject oPC, int perkID)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return -1;

        PlayerGO pcGO = new PlayerGO(oPC);
        PerkRepository perkRepo = new PerkRepository();
        PCPerksEntity pcPerk = perkRepo.GetPCPerkByID(pcGO.getUUID(), perkID);
        return pcPerk == null ? 0 : pcPerk.getPerkLevel();
    }
}
