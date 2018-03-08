package Conversation;


import Dialog.*;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Data.Repository.PlayerRepository;
import GameSystems.ProgressionSystem;
import org.joda.time.*;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class ResetSkillPoints extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage();
        DialogPage confirmResetPage = new DialogPage(
                "Are you sure you want to reset your skills? All purchases will be reset and your skill points will be returned to you.\n\nIn addition, all items will be unequipped from you.",
                "Confirm Reset Skill Points",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("ConfirmResetPage", confirmResetPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        HandleResetTokenConferrence();
        SetPageHeader("MainPage", BuildHeader());
        BuildDialogResponses();
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        HandleResetTokenConferrence();

        switch(pageName)
        {
            case "MainPage": {
                switch (responseID)
                {
                    case 1: // Refresh
                        SetPageHeader("MainPage", BuildHeader());
                        BuildDialogResponses();
                        break;
                    case 2: // Reset Skills
                        ChangePage("ConfirmResetPage");
                        break;
                    case 3: // Back
                        ClearTempVariables();
                        SwitchConversation("CharacterManagement");
                        break;
                }
                break;
            }
            case "ConfirmResetPage": {
                switch (responseID)
                {
                    case 1: // Confirm / REALLY CONFIRM Skill Reset
                        HandleConfirmReset();
                        break;
                    case 2: // Back
                        ClearTempVariables();
                        ChangePage("MainPage");
                        break;
                }
            }
        }
    }

    @Override
    public void EndDialog() {
        ClearTempVariables();
    }

    private void HandleResetTokenConferrence()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());

        DateTime nextTokenDate = entity.getNextResetTokenReceiveDate() == null ||
                entity.getResetTokens() >= ProgressionSystem.ResetTokensMaxStock ?
                    DateTime.now().plusWeeks(2) :
                    new DateTime(entity.getNextResetTokenReceiveDate());

        if(DateTime.now().isAfter(nextTokenDate))
        {
            entity.setResetTokens(entity.getResetTokens() + 1);
            nextTokenDate = DateTime.now().plusWeeks(2);
        }

        entity.setNextResetTokenReceiveDate(nextTokenDate.toDate());
        repo.save(entity);
    }

    private String BuildHeader()
    {
        NWObject oPC = GetPC();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());

        String resetTime = "now.";

        if(entity.getNextSPResetDate() != null)
        {
            DateTime resetDate = new DateTime(entity.getNextSPResetDate());
            DateTime now = DateTime.now(DateTimeZone.UTC);


            if(resetDate.isAfter(now))
            {
                Period period = new Period(now, resetDate);


                resetTime = "in ";
                resetTime += period.getDays() + (period.getDays() == 1 ? " day, " : " days, ");
                resetTime += period.getHours() + (period.getHours() == 1 ? " hour, " : " hours, ");
                resetTime += period.getMinutes() + (period.getMinutes() == 1 ? " minute, " : " minutes, ");
                resetTime += period.getSeconds() + (period.getSeconds() == 1 ? " second." : " seconds.");
            }

        }

        String header = "You may reset your skill point allocation here. ";
        header += "Resets may only be performed once every 72 real-world hours and require 'Reset Tokens'. ";
        header += "You receive a new reset token once every 2 real-world weeks. ";
        header += "A maximum of 3 reset tokens may be stored at one time.\n\n";
        header += ColorToken.Green() + "# of Reset Tokens: " + ColorToken.End() + entity.getResetTokens() + "\n";
        header += ColorToken.Green() + "You can reset your skills " + resetTime + "\n\n";

        return header;
    }

    private void BuildDialogResponses()
    {
        NWObject oPC = GetPC();
        DialogPage page = GetPageByName("MainPage");
        page.getResponses().clear();
        page.getResponses().add(new DialogResponse(ColorToken.Green() + "Refresh" + ColorToken.End()));

        DialogResponse response = new DialogResponse("Reset Skills");

        if(!ProgressionSystem.CanResetSkills(oPC))
        {
            response.setActive(false);
        }

        page.getResponses().add(response);
        page.getResponses().add(new DialogResponse("Back"));
    }

    private void ClearTempVariables()
    {
        NWObject oPC = GetPC();
        NWScript.deleteLocalInt(oPC, "TEMP_MENU_REALLY_CONFIRM");
        SetResponseText("ConfirmResetPage", 1, "Confirm Reset Skill Points");
    }

    private void HandleConfirmReset()
    {
        NWObject oPC = GetPC();
        boolean isReallyConfirm = NWScript.getLocalInt(oPC, "TEMP_MENU_REALLY_CONFIRM") == 1;

        if(isReallyConfirm)
        {
            NWScript.deleteLocalInt(oPC, "TEMP_MENU_REALLY_CONFIRM");
            SetResponseText("ConfirmResetPage", 1, "Confirm Reset Skill Points");
            ProgressionSystem.PerformSkillReset(oPC, false);
            HandleResetTokenConferrence();
            SetPageHeader("MainPage", BuildHeader());
            BuildDialogResponses();
            ChangePage("MainPage");
        }
        else
        {
            NWScript.setLocalInt(oPC, "TEMP_MENU_REALLY_CONFIRM", 1);
            SetResponseText("ConfirmResetPage", 1, ColorToken.Red() + "REALLY CONFIRM RESET SKILL POINTS (LAST CHANCE TO BACK OUT)" + ColorToken.End());
        }

    }
}
