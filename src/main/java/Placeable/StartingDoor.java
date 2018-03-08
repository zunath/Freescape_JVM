package Placeable;

import Dialog.DialogManager;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class StartingDoor implements IScriptEventHandler {
    @Override
    public void runScript(NWObject door) {
        NWObject oPC = NWScript.getLastUsedBy();
        NWObject professionToken = NWScript.getItemPossessedBy(oPC, "prof_token");

        if(NWScript.getIsObjectValid(professionToken))
        {
            NWScript.floatingTextStringOnCreature("You must select a profession before entering the game world!", oPC, false);
            DialogManager.startConversation(oPC, oPC, "ProfessionMenu");
        }
        else
        {
            DialogManager.startConversation(oPC, oPC, "StartingDoor");
        }
    }
}
