package Conversation.ViewModels;

public class HeadViewModel {
    private int originalHeadID;
    private int currentHeadID;

    public HeadViewModel(int originalHeadID)
    {
        this.originalHeadID = originalHeadID;
        this.currentHeadID = originalHeadID;
    }

    public int getOriginalHeadID() {
        return originalHeadID;
    }

    public void setOriginalHeadID(int originalHeadID) {
        this.originalHeadID = originalHeadID;
    }

    public int getCurrentHeadID() {
        return currentHeadID;
    }

    public void setCurrentHeadID(int currentHeadID) {
        this.currentHeadID = currentHeadID;
    }
}
