package GameSystems;

import ChatCommands.IChatCommand;
import Helper.ColorToken;
import Helper.ScriptHelper;
import NWNX.NWNX_Chat;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class ChatCommandSystem {

    public static void OnModuleNWNXChat(NWObject sender)
    {
        if(!getIsPC(sender) || getIsDM(sender) || getIsDMPossessed(sender)) return;

        String message = NWNX_Chat.GetMessage().trim().toLowerCase();

        // If is double slash (//) treat it as a normal message (this is used by role-players to denote OOC speech)
        if(message.substring(0, 2).equals("//")) return;

        if(!message.substring(0, 1).equals("/"))
        {
            return;
        }

        String command = message.substring(1, message.length());
        command = command.substring(0, 1).toUpperCase() + command.substring(1);
        NWNX_Chat.SkipMessage();

        IChatCommand chatCommand = (IChatCommand) ScriptHelper.GetClassByName("ChatCommands." + command);
        if(chatCommand == null || !chatCommand.CanUse(sender))
        {
            sendMessageToPC(sender, ColorToken.Red() + "Command not found." + ColorToken.End());
            return;
        }

        chatCommand.DoAction(sender);
    }
}
