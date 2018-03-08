package Conversation.NPC;

import Data.Repository.PlayerRepository;
import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import GameSystems.DeathSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;

public class RevivalWizard extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "",
                "I want to use a revival stone to return to life."
        );

        dialog.addPage("MainPage", mainPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        PlayerRepository repo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());

        String header = "Greetings, mortal. It appears you have left your dimension but your soul still has unfinished business. Should you desire to return to the land of the living I will request one Revival Stone.\n\n";
        header += "You have " + ColorToken.Green() + entity.getRevivalStoneCount() + ColorToken.End() + " revival stones.";
        SetPageHeader("MainPage", header);

        if(entity.getRevivalStoneCount() <= 0)
            SetResponseVisible("MainPage", 1, false);
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        if(responseID == 1)
        {
            DeathSystem.RespawnPlayer(oPC);
        }
    }

    @Override
    public void EndDialog() {

    }
}
