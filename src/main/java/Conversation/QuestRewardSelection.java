package Conversation;

import Conversation.ViewModels.QuestRewardViewModel;
import Data.Repository.QuestRepository;
import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Entities.QuestEntity;
import Entities.QuestRewardItemEntity;
import Entities.QuestStateEntity;
import GameSystems.Models.ItemModel;
import GameSystems.QuestSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class QuestRewardSelection extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "Please select a reward."
        );

        dialog.addPage("MainPage", mainPage);
        return dialog;
    }

    @Override
    public void Initialize() {

        int questID = NWScript.getLocalInt(GetPC(), "QST_REWARD_SELECTION_QUEST_ID");
        NWScript.deleteLocalInt(GetPC(), "QST_REWARD_SELECTION_QUEST_ID");
        QuestRepository repo = new QuestRepository();
        QuestEntity quest = repo.GetQuestByID(questID);

        QuestRewardViewModel model = new QuestRewardViewModel();
        model.setIsItemSelected(false);
        model.setQuestID(questID);

        DialogPage page = GetCurrentPage();
        page.setCustomData(model);

        for(QuestRewardItemEntity reward: quest.getRewardItems())
        {
            ItemModel tempItem = QuestSystem.GetTempItemInformation(reward.getResref(), reward.getQuantity());
            String rewardName = tempItem.getName();
            if(tempItem.getQuantity() > 1)
                rewardName += " x" + tempItem.getQuantity();

            page.addResponse(rewardName, true, tempItem);
        }

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
            {
                HandleRewardSelection(responseID);
                break;
            }
        }
    }

    private void HandleRewardSelection(int responseID)
    {
        DialogPage page = GetCurrentPage();
        QuestRewardViewModel model = (QuestRewardViewModel) page.getCustomData();
        ItemModel tempItem = (ItemModel) GetResponseByID("MainPage", responseID).getCustomData();
        QuestRepository repo = new QuestRepository();
        QuestEntity quest = repo.GetQuestByID(model.getQuestID());

        QuestStateEntity lastState = quest.getQuestStates().get(quest.getQuestStates().size()-1);
        NWScript.addJournalQuestEntry(quest.getJournalTag(), lastState.getJournalStateID(), GetPC(), false, false, false);
        QuestSystem.CompleteQuest(GetPC(), model.getQuestID(), tempItem);
        model.setIsItemSelected(true);

        EndConversation();
    }

    @Override
    public void EndDialog() {
        QuestRewardViewModel model = (QuestRewardViewModel)GetCurrentPage().getCustomData();
        QuestRepository repo = new QuestRepository();
        QuestEntity quest = repo.GetQuestByID(model.getQuestID());

        if(!model.isItemSelected())
        {
            // TODO: Return submitted items.
        }
    }
}
