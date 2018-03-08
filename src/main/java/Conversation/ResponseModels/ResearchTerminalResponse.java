package Conversation.ResponseModels;

public class ResearchTerminalResponse {
    private int slotNumber;
    private boolean isReadyToDeliver;
    private boolean isResearching;

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public boolean isReadyToDeliver() {
        return isReadyToDeliver;
    }

    public void setReadyToDeliver(boolean readyToDeliver) {
        isReadyToDeliver = readyToDeliver;
    }

    public boolean isResearching() {
        return isResearching;
    }

    public void setResearching(boolean researching) {
        isResearching = researching;
    }
}
