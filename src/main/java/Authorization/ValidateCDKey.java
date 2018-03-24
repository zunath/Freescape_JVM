package Authorization;

import Common.IScriptEventHandler;
import Data.Repository.AuthorizedDMRepository;
import Entities.AuthorizedDMEntity;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class ValidateCDKey implements IScriptEventHandler {
    @Override
    public void runScript(NWObject pc) {

        String sCDKey = NWScript.getPCPublicCDKey(pc, false);
        String sPlayerName = NWScript.getPCPlayerName(pc);
        String sIP = NWScript.getPCIPAddress(pc);
        boolean isDM = false;
        AuthorizedDMRepository repo = new AuthorizedDMRepository();

        if (NWScript.getIsDM(pc) || (NWScript.getIsDMPossessed(pc) && NWScript.getIsDM(NWScript.getMaster(pc))))
        {
            AuthorizedDMEntity entity = repo.GetDMByCDKey(sCDKey);

            if(entity != null && entity.getDMRole() > 0)
            {
                isDM = true;
            }

            if (isDM)
            {
                NWScript.sendMessageToAllDMs("<DM authorized to join server, " + sCDKey + ", " + sPlayerName + ">" );
            }
            else
            {
                NWScript.sendMessageToAllDMs("<WARNING, UNAUTHORIZED DM JOIN ATTEMPT BY " + sCDKey + ", " + sPlayerName + ", " + sIP + ">" );
                NWScript.bootPC(pc, "You are not authorized to join as a DM.");
            }
        }
    }
}
