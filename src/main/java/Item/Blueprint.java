package Item;

import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.CraftRepository;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class Blueprint implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oItem = NWScript.getItemActivated();
        String resref = NWScript.getResRef(oItem);
        int blueprintID = Integer.parseInt(resref.substring(10));

        if(blueprintID <= 0)
        {
            NWScript.sendMessageToPC(oPC, "Unable to add blueprint.");
            return;
        }

        CraftRepository repo = new CraftRepository();
        boolean success = repo.AddBlueprintToPC(pcGO.getUUID(), blueprintID);

        if(success)
        {
            NWScript.destroyObject(oItem, 0.0f);
            NWScript.sendMessageToPC(oPC, "You add the blueprint to your collection.");
        }
        else
        {
            NWScript.sendMessageToPC(oPC, "You already added that blueprint to your collection.");
        }

    }
}
