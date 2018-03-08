package Conversation.ViewModels;

import Entities.ProfessionEntity;

public class ProfessionViewModel {

    private ProfessionEntity selectedEntity;
    private boolean isConfirming;

    public ProfessionEntity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(ProfessionEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public boolean isConfirming() {
        return isConfirming;
    }

    public void setConfirming(boolean confirming) {
        isConfirming = confirming;
    }
}
