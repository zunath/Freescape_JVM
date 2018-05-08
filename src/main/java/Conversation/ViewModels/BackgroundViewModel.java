package Conversation.ViewModels;


import Entities.BackgroundEntity;

public class BackgroundViewModel {

    private BackgroundEntity selectedEntity;
    private boolean isConfirming;

    public BackgroundEntity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(BackgroundEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public boolean isConfirming() {
        return isConfirming;
    }

    public void setConfirming(boolean confirming) {
        isConfirming = confirming;
    }
}
