package Authorization;

import Common.IScriptEventHandler;
import Entities.AuthorizedDMEntity;
import Data.Repository.AuthorizedDMRepository;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class ValidateDM implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        String cdKey = NWScript.getPCPublicCDKey(objSelf, false);
        AuthorizedDMRepository repo = new AuthorizedDMRepository();
        AuthorizedDMEntity entity = repo.GetDMByCDKey(cdKey);
        int isDM = 0;

        if(entity != null && entity.getDMRole() == 1) // 1 = DM
        {
            isDM = 1;
        }

        NWScript.setLocalInt(objSelf, "AUTH_IS_DM", isDM);
    }
}
