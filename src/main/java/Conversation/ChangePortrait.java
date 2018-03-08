package Conversation;

import Conversation.ViewModels.PortraitViewModel;
import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Entities.PortraitEntity;
import Helper.ColorToken;
import Data.Repository.PortraitRepository;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class ChangePortrait extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "<SET LATER>",
                "Advance Portrait ID by 1",
                "Advance Portrait ID by 10",
                "Advance Portrait ID by 100",
                "Decrease Portrait ID by 1",
                "Decrease Portrait ID by 10",
                "Decrease Portrait ID by 100",
                "Set Portrait",
                "Reset Portrait",
                "Back"
         );

        dialog.addPage("MainPage", mainPage);

        return dialog;
    }

    @Override
    public void Initialize()
    {
        NWObject oPC = GetPC();
        PortraitEntity entity;
        PortraitRepository repo = new PortraitRepository();

        int portraitID = NWScript.getPortraitId(oPC);
        if(portraitID == 65535) // Invalid portrait
        {
            String portraitResref = NWScript.getPortraitResRef(oPC).substring(3);
            entity = repo.GetByResref(portraitResref);
            if(entity == null)
            {
                portraitID = 0;
            }
            else
            {
                portraitID = entity.getPortraitID();
            }
        }

        entity = repo.GetBy2DAID(portraitID);

        SetDialogCustomData(new PortraitViewModel(entity.getPortraitID()));
        SetPageHeader("MainPage", BuildHeader());
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
                        ChangePortraitID(1);
                        break;
                    case 2: // Advance by 10
                        ChangePortraitID(10);
                        break;
                    case 3: // Advance by 100
                        ChangePortraitID(100);
                        break;
                    case 4: // Decrease by 1
                        ChangePortraitID(-1);
                        break;
                    case 5: // Decrease by 10
                        ChangePortraitID(-10);
                        break;
                    case 6: // Decrease by 100
                        ChangePortraitID(-100);
                        break;
                    case 7: // Set Portrait
                        SetPortraitID();
                        break;
                    case 8: // Reset Portrait
                        ResetPortraitID();
                        break;
                    case 9: // Back
                        ResetPortraitID();
                        SwitchConversation("CharacterManagement");
                        break;
                }

                break;
            }
        }
    }

    @Override
    public void EndDialog() {
        ResetPortraitID();
    }

    private void ChangePortraitID(int adjustBy)
    {
        NWObject oPC = GetPC();
        PortraitViewModel dto = (PortraitViewModel)GetDialogCustomData();
        PortraitRepository repo = new PortraitRepository();
        int numberOfPortraits = repo.GetNumberOfPortraits();

        if(dto.getCurrentPortraitID() > numberOfPortraits)
            dto.setCurrentPortraitID(numberOfPortraits);
        if(dto.getOriginalPortraitID() > numberOfPortraits)
            dto.setOriginalPortraitID(numberOfPortraits);

        dto.setCurrentPortraitID(dto.getCurrentPortraitID() + adjustBy);


        if(dto.getCurrentPortraitID() > numberOfPortraits)
        {
            dto.setCurrentPortraitID(dto.getCurrentPortraitID() - numberOfPortraits);
        }

        if(dto.getCurrentPortraitID() < 1)
        {
            dto.setCurrentPortraitID(numberOfPortraits + adjustBy);
        }

        PortraitEntity entity = repo.GetByPortraitID(dto.getCurrentPortraitID());
        NWScript.setPortraitId(oPC, entity.get2DAID());

        SetDialogCustomData(dto);
        SetPageHeader("MainPage", BuildHeader());
    }

    private void ResetPortraitID()
    {
        NWObject oPC = GetPC();
        PortraitViewModel dto = (PortraitViewModel)GetDialogCustomData();
        PortraitRepository repo = new PortraitRepository();
        dto.setCurrentPortraitID(dto.getOriginalPortraitID());

        PortraitEntity entity = repo.GetByPortraitID(dto.getOriginalPortraitID());
        NWScript.setPortraitId(oPC, entity.get2DAID());

        SetDialogCustomData(dto);
        SetPageHeader("MainPage", BuildHeader());
    }

    private void SetPortraitID()
    {
        NWObject oPC = GetPC();
        PortraitViewModel dto = (PortraitViewModel)GetDialogCustomData();
        dto.setOriginalPortraitID(dto.getCurrentPortraitID());

        SetDialogCustomData(dto);
        SetPageHeader("MainPage", BuildHeader());
    }

    private String BuildHeader()
    {
        NWObject oPC = GetPC();

        PortraitViewModel dto = (PortraitViewModel)GetDialogCustomData();
        String header = "You may adjust your character's portrait here. Open your character sheet to view your current portrait ('C' by default)." + "\n\n";
        header += ColorToken.Green() + "Current Set Portrait ID: " + ColorToken.End() + "%originalID% " + "\n";
        header += ColorToken.Green() + "Viewing Portrait ID: " + ColorToken.End() + "%currentID% " + "\n";

        if(dto == null)
        {
            header = header.replace("%originalID%", "" + NWScript.getPortraitId(oPC));
            header = header.replace("%currentID%", "" + NWScript.getPortraitId(oPC));
        }
        else
        {

            header = header.replace("%originalID%", "" + dto.getOriginalPortraitID());
            header = header.replace("%currentID%", "" + dto.getCurrentPortraitID());
        }

        return header;
    }
}
