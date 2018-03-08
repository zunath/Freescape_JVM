package Conversation.ViewModels;

public class QuestRewardViewModel {
    private int questID;
    private boolean itemSelected;

    public int getQuestID() {
        return questID;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public boolean isItemSelected() {
        return itemSelected;
    }

    public void setIsItemSelected(boolean itemWasSelected) {
        this.itemSelected = itemWasSelected;
    }
}
