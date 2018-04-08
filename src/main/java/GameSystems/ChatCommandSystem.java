package GameSystems;

import NWNX.NWNX_Chat;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class ChatCommandSystem {

    public static void OnModuleNWNXChat(NWObject sender)
    {
        if(!getIsPC(sender) || getIsDM(sender) || getIsDMPossessed(sender)) return;

        String message = NWNX_Chat.GetMessage().trim();

        if(message.equals("/stuck"))
        {
            DeathSystem.TeleportPlayerToBindPoint(sender);
            NWNX_Chat.SkipMessage();
            sendMessageToPC(sender, "Alpha feature: Returning to bind point. Please report bugs on Discord/GitHub. And for the love of all that is Zunath, don't abuse this!");
        }
    }
}
