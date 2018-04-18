package GameSystems;

import NWNX.NWNX_Chat;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class PlayerDescriptionSystem {

    public static void OnModuleNWNXChat(NWObject sender)
    {
        if(getLocalInt(sender, "LISTENING_FOR_DESCRIPTION") != 1) return;
        if(!getIsPC(sender) || getIsDM(sender) || getIsDMPossessed(sender)) return;

        String text = NWNX_Chat.GetMessage().trim();
        setLocalString(sender, "NEW_DESCRIPTION_TO_SET", text);
        NWNX_Chat.SkipMessage();

        sendMessageToPC(sender, "New description received. Please press the 'Next' button in the conversation window.");
    }

    public static void ChangePlayerDescription(NWObject oPC)
    {
        String newDescription = getLocalString(oPC, "NEW_DESCRIPTION_TO_SET");
        setDescription(oPC, newDescription, false);
        setDescription(oPC, newDescription, true);

        floatingTextStringOnCreature("New description set!", oPC, false);
    }
}
