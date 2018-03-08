package Conversation.ViewModels;

public class ResearchTerminalViewModel {

    private int selectedSlot;

    private int selectedCraftID;

    private int selectedCraftCategoryID;

    private int selectedBlueprintID;

    private int playerResearchLevel;

    private boolean isConfirmingBlueprintSelection;

    private boolean isConfirmingCancelResearch;

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public int getSelectedCraftCategoryID() {
        return selectedCraftCategoryID;
    }

    public void setSelectedCraftCategoryID(int selectedCraftCategoryID) {
        this.selectedCraftCategoryID = selectedCraftCategoryID;
    }

    public int getSelectedCraftID() {
        return selectedCraftID;
    }

    public void setSelectedCraftID(int selectedCraftID) {
        this.selectedCraftID = selectedCraftID;
    }

    public int getSelectedBlueprintID() {
        return selectedBlueprintID;
    }

    public void setSelectedBlueprintID(int selectedBlueprintID) {
        this.selectedBlueprintID = selectedBlueprintID;
    }

    public int getPlayerResearchLevel() {
        return playerResearchLevel;
    }

    public void setPlayerResearchLevel(int playerResearchLevel) {
        this.playerResearchLevel = playerResearchLevel;
    }

    public boolean isConfirmingBlueprintSelection() {
        return isConfirmingBlueprintSelection;
    }

    public void setConfirmingBlueprintSelection(boolean confirmingBlueprintSelection) {
        isConfirmingBlueprintSelection = confirmingBlueprintSelection;
    }

    public boolean isConfirmingCancelResearch() {
        return isConfirmingCancelResearch;
    }

    public void setConfirmingCancelResearch(boolean confirmingCancelResearch) {
        isConfirmingCancelResearch = confirmingCancelResearch;
    }
}
