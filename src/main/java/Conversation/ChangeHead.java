package Conversation;

import Conversation.ViewModels.HeadViewModel;
import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import org.nwnx.nwnx2.jvm.NWObject;

import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.CreaturePart;

@SuppressWarnings("UnusedDeclaration")
public class ChangeHead extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "<SET LATER>",
                "Advance Head ID by 1",
                "Advance Head ID by 10",
                "Advance Head ID by 100",
                "Decrease Head ID by 1",
                "Decrease Head ID by 10",
                "Decrease Head ID by 100",
                "Set Head",
                "Reset Head",
                "Back"
        );

        mainPage.setCustomData(new HeadViewModel(NWScript.getCreatureBodyPart(CreaturePart.HEAD, oPC)));
        mainPage.setHeader(BuildHeader());

        dialog.addPage("MainPage", mainPage);

        return dialog;
    }

    @Override
    public void Initialize()
    {

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
            {
                switch (responseID)
                {
                    case 1: // Advance by 1
                        ChangeHeadID(1);
                        break;
                    case 2: // Advance by 10
                        ChangeHeadID(10);
                        break;
                    case 3: // Advance by 100
                        ChangeHeadID(100);
                        break;
                    case 4: // Decrease by 1
                        ChangeHeadID(-1);
                        break;
                    case 5: // Decrease by 10
                        ChangeHeadID(-10);
                        break;
                    case 6: // Decrease by 100
                        ChangeHeadID(-100);
                        break;
                    case 7: // Set Portrait
                        SetHeadID();
                        break;
                    case 8: // Reset Portrait
                        ResetHeadID();
                        break;
                    case 9: // Back
                        ResetHeadID();
                        SwitchConversation("CharacterManagement");
                        break;
                }

                break;
            }
        }
    }

    @Override
    public void EndDialog() {
        ResetHeadID();
    }


    private void ChangeHeadID(int adjustBy)
    {
        NWObject oPC = GetPC();
        HeadViewModel dto = (HeadViewModel)GetCurrentPage().getCustomData();
        dto.setCurrentHeadID(dto.getCurrentHeadID() + adjustBy);
        int numberOfHeads = 202;

        if(dto.getCurrentHeadID() > numberOfHeads)
        {
            dto.setCurrentHeadID(dto.getCurrentHeadID() - numberOfHeads);
        }

        if(dto.getCurrentHeadID() < 1)
        {
            dto.setCurrentHeadID(numberOfHeads + dto.getCurrentHeadID());
        }

        NWScript.setCreatureBodyPart(CreaturePart.HEAD, dto.getCurrentHeadID(), oPC);

        GetCurrentPage().setCustomData(dto);
        SetPageHeader("MainPage", BuildHeader());
    }

    private void ResetHeadID()
    {
        NWObject oPC = GetPC();
        HeadViewModel dto = (HeadViewModel)GetCurrentPage().getCustomData();
        dto.setCurrentHeadID(dto.getOriginalHeadID());

        NWScript.setCreatureBodyPart(CreaturePart.HEAD, dto.getOriginalHeadID(), oPC);

        GetCurrentPage().setCustomData(dto);
        SetPageHeader("MainPage", BuildHeader());
    }

    private void SetHeadID()
    {
        NWObject oPC = GetPC();
        HeadViewModel dto = (HeadViewModel)GetCurrentPage().getCustomData();
        dto.setOriginalHeadID(dto.getCurrentHeadID());

        GetCurrentPage().setCustomData(dto);
        SetPageHeader("MainPage", BuildHeader());
    }

    private String BuildHeader()
    {
        NWObject oPC = GetPC();

        HeadViewModel dto = (HeadViewModel)GetCurrentPage().getCustomData();
        String header = "You may adjust your character's head here." + "\n\n";
        header += ColorToken.Green() + "Current Set Head ID: " + ColorToken.End() + "%originalID% " + "\n";
        header += ColorToken.Green() + "Viewing Head ID: " + ColorToken.End() + "%currentID% " + "\n";

        if(dto == null)
        {
            header = header.replace("%originalID%", "" + NWScript.getCreatureBodyPart(CreaturePart.HEAD, oPC));
            header = header.replace("%currentID%", "" + NWScript.getCreatureBodyPart(CreaturePart.HEAD, oPC));
        }
        else
        {

            header = header.replace("%originalID%", "" + dto.getOriginalHeadID());
            header = header.replace("%currentID%", "" + dto.getCurrentHeadID());
        }

        return header;
    }
}
