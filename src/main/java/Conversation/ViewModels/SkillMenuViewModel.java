package Conversation.ViewModels;

public class SkillMenuViewModel {
    private int selectedCategoryID;
    private int selectedSkillID;

    public SkillMenuViewModel()
    {
    }

    public int getSelectedCategoryID() {
        return selectedCategoryID;
    }

    public void setSelectedCategoryID(int selectedCategoryID) {
        this.selectedCategoryID = selectedCategoryID;
    }

    public int getSelectedSkillID() {
        return selectedSkillID;
    }

    public void setSelectedSkillID(int selectedSkillID) {
        this.selectedSkillID = selectedSkillID;
    }

}
