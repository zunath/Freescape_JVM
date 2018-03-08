package Dialog;

import GameObject.PlayerGO;
import Helper.ErrorHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("UnusedDeclaration")
public abstract class DialogBase {

    protected NWObject GetPC()
    {
        return NWScript.getPCSpeaker();
    }

    protected NWObject GetDialogTarget()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        return dialog.getDialogTarget();
    }

    protected Object GetDialogCustomData()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        return dialog.getDialogCustomData();
    }

    protected void SetDialogCustomData(Object data)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        dialog.setDialogCustomData(data);
    }

    protected void ChangePage(String pageName)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        dialog.setCurrentPageName(pageName);
        dialog.setPageOffset(0);
    }

    protected void SetPageHeader(String pageName, String header)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        DialogPage page = dialog.getPageByName(pageName);
        page.setHeader(header);
    }

    protected DialogPage GetPageByName(String pageName)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        return dialog.getPageByName(pageName);
    }

    protected DialogPage GetCurrentPage()
    {
        return GetPageByName(GetCurrentPageName());
    }

    protected String GetCurrentPageName()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        return dialog.getCurrentPageName();
    }


    protected DialogResponse GetResponseByID(String pageName, int responseID)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        DialogPage page = dialog.getPageByName(pageName);
        return page.getResponses().get(responseID - 1);
    }

    protected void SetResponseText(String pageName, int responseID, String responseText)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        DialogPage page = dialog.getPageByName(pageName);
        page.getResponses().get(responseID - 1).setText(responseText);
    }

    protected void SetResponseVisible(String pageName, int responseID, boolean isVisible)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        DialogPage page = dialog.getPageByName(pageName);
        page.getResponses().get(responseID - 1).setActive(isVisible);
    }

    protected void AddResponseToPage(String pageName, String text, boolean isVisible)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        DialogPage page = dialog.getPageByName(pageName);
        page.addResponse(text, isVisible);
    }

    protected void AddResponseToPage(String pageName, String text, boolean isVisible, Object customData)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        DialogPage page = dialog.getPageByName(pageName);
        page.addResponse(text, isVisible, customData);
    }

    protected void ClearPageResponses(String pageName)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        DialogPage page = dialog.getPageByName(pageName);
        page.getResponses().clear();
    }

    protected void SwitchConversation(String conversationName)
    {
        NWObject oPC = GetPC();
        NWObject oTarget = GetDialogTarget();
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());

        try
        {
            DialogManager.loadConversation(GetPC(), dialog.getDialogTarget(), conversationName, dialog.getDialogNumber());
        }
        catch (Exception ex)
        {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();

            String message = "Error running DialogBase.SwitchConversation();";
            System.out.println(message);
            System.out.println("Exception: ");
            System.out.println(exceptionAsString);

            NWScript.writeTimestampedLogEntry(message);
            NWScript.writeTimestampedLogEntry("Exception:");
            NWScript.writeTimestampedLogEntry(exceptionAsString);
        }
        dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());

        dialog.resetPage();

        ChangePage(dialog.getCurrentPageName());

        try
        {
            Class scriptClass = Class.forName("Conversation." + dialog.getActiveDialogName());
            IDialogHandler script = (IDialogHandler)scriptClass.newInstance();
            script.Initialize();
            NWScript.setLocalInt(oPC, "DIALOG_SYSTEM_INITIALIZE_RAN", 1);
        }
        catch (Exception ex)
        {
            ErrorHelper.HandleException(ex, "Unable to initialize conversation: " + dialog.getActiveDialogName());
        }
    }

    protected void EndConversation()
    {
        DialogManager.endConversation(GetPC());
    }

}
