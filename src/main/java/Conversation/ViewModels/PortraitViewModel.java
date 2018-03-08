package Conversation.ViewModels;

public class PortraitViewModel {

    private int originalPortraitID;
    private int currentPortraitID;

    public PortraitViewModel(int originalPortraitID)
    {
        this.originalPortraitID = originalPortraitID;
        this.currentPortraitID = originalPortraitID;
    }

    public int getOriginalPortraitID() {
        return originalPortraitID;
    }

    public void setOriginalPortraitID(int originalPortraitID) {
        this.originalPortraitID = originalPortraitID;
    }

    public int getCurrentPortraitID() {
        return currentPortraitID;
    }

    public void setCurrentPortraitID(int currentPortraitID) {
        this.currentPortraitID = currentPortraitID;
    }
}
