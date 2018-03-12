package Conversation.ViewModels;

public class PerkMenuViewModel {

    private int selectedCategoryID;
    private int selectedPerkID;

    public int getSelectedCategoryID() {
        return selectedCategoryID;
    }

    public void setSelectedCategoryID(int selectedCategoryID) {
        this.selectedCategoryID = selectedCategoryID;
    }

    public int getSelectedPerkID() {
        return selectedPerkID;
    }

    public void setSelectedPerkID(int selectedPerkID) {
        this.selectedPerkID = selectedPerkID;
    }
}
