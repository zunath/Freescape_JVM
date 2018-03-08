package Dialog;

import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ErrorHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.HashMap;

public class DialogManager {

    public static final int NumberOfResponsesPerPage = 12;
    private static final int NumberOfDialogs = 96;

    private static HashMap<String, PlayerDialog> playerDialogs;
    private static HashMap<Integer, Boolean> dialogFilesInUse;

    static {
        playerDialogs = new HashMap<>();
        dialogFilesInUse = new HashMap<>();

        for(int x = 1; x <= NumberOfDialogs; x++)
        {
            dialogFilesInUse.put(x, false);
        }
    }

    private static void storePlayerDialog(String uuid, PlayerDialog dialog)
    {
        if(dialog.getDialogNumber() <= 0)
        {
            for(int x = 1; x <= NumberOfDialogs; x++)
            {
                if(!dialogFilesInUse.get(x))
                {
                    dialogFilesInUse.put(x, true);
                    dialog.setDialogNumber(x);
                    break;
                }
            }
        }

        // Couldn't find an open dialog file. Throw error.
        if(dialog.getDialogNumber() <= 0)
        {
            System.out.println("ERROR: Unable to locate a free dialog. Add more dialog files, update their custom tokens, and update DialogManager.java");
            return;
        }

        playerDialogs.put(uuid, dialog);
    }

    public static PlayerDialog loadPlayerDialog(String uuid)
    {
        return playerDialogs.getOrDefault(uuid, null);
    }

    public static void removePlayerDialog(String uuid)
    {
        // Free up the dialog file for another player.
        PlayerDialog dialog = playerDialogs.get(uuid);
        dialogFilesInUse.put(dialog.getDialogNumber(), false);

        // Remove the player's dialog info
        playerDialogs.remove(uuid);
    }

    public static void loadConversation(NWObject oPC, NWObject oTalkTo, String conversationName, int dialogNumber)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        Class scriptClass = Class.forName("Conversation." + conversationName);
        IDialogHandler script = (IDialogHandler)scriptClass.newInstance();
        PlayerDialog dialog = script.SetUp(oPC);
        if(dialogNumber > 0)
            dialog.setDialogNumber(dialogNumber);

        dialog.setActiveDialogName(conversationName);
        dialog.setDialogTarget(oTalkTo);
        DialogManager.storePlayerDialog(pcGO.getUUID(), dialog);

    }

    public static void startConversation(NWObject oPC, final NWObject oTalkTo, final String conversationName)
    {
        try {
            PlayerGO pcGO = new PlayerGO(oPC);
            String uuid = pcGO.getUUID();

            loadConversation(oPC, oTalkTo, conversationName, -1);

            PlayerDialog dialog = playerDialogs.get(uuid);

            if(dialog.getDialogNumber() <= 0)
            {
                NWScript.floatingTextStringOnCreature(ColorToken.Red() + "ERROR: No dialog files are available for use." + ColorToken.End(), oPC, false);
                return;
            }

            // NPC conversations
            if(NWScript.getObjectType(oTalkTo) == ObjectType.CREATURE &&
                    !NWScript.getIsPC(oTalkTo) &&
                    !NWScript.getIsDM(oTalkTo) &&
                    !NWScript.getIsDMPossessed(oTalkTo))
            {
                NWScript.beginConversation("dialog" + dialog.getDialogNumber(), NWObject.INVALID);
            }
            // Everything else
            else
            {
                Scheduler.assign(oPC, () -> NWScript.actionStartConversation(oTalkTo, "dialog" + dialog.getDialogNumber(), true, false));
            }
        }
        catch(Exception ex) {
            ErrorHelper.HandleException(ex, "DialogStart was unable to execute class method: Conversation." + conversationName + ".Initialize()");
        }
    }

    public static void endConversation(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerDialog playerDialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        playerDialog.setIsEnding(true);
        DialogManager.storePlayerDialog(pcGO.getUUID(), playerDialog);
    }

}
