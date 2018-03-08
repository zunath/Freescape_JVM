package GameSystems;

import Data.Repository.AuthorizedDMRepository;
import Entities.AuthorizedDMEntity;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class PlayerAuthorizationSystem {

    public static boolean IsPCRegisteredAsDM(NWObject oPC)
    {
        if(NWScript.getIsDM(oPC)) return true;
        if(!NWScript.getIsPC(oPC)) return false;

        String cdKey = NWScript.getPCPublicCDKey(oPC, false);
        AuthorizedDMRepository repo = new AuthorizedDMRepository();

        AuthorizedDMEntity entity = repo.GetDMByCDKey(cdKey);
        return entity != null;

    }
}
