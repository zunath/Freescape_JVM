package Conversation.ViewModels;

public class ConstructionSiteMenuModel {

    private int constructionSiteID;
    private boolean isPreviewing;
    private int categoryID;
    private int blueprintID;
    private boolean isTerritoryFlag;
    private String recoverResourcesHeader;
    private int flagID;

    public int getConstructionSiteID() {
        return constructionSiteID;
    }

    public void setConstructionSiteID(int constructionSiteID) {
        this.constructionSiteID = constructionSiteID;
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

    public void setIsPreviewing(boolean isPreviewing) {
        this.isPreviewing = isPreviewing;
    }

    public int getBlueprintID() {
        return blueprintID;
    }

    public void setBlueprintID(int blueprintID) {
        this.blueprintID = blueprintID;
    }

    public boolean isTerritoryFlag() {
        return isTerritoryFlag;
    }

    public void setIsTerritoryFlag(boolean isTerritoryFlag) {
        this.isTerritoryFlag = isTerritoryFlag;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getRecoverResourcesHeader() {
        return recoverResourcesHeader;
    }

    public void setRecoverResourcesHeader(String recoverResourcesHeader) {
        this.recoverResourcesHeader = recoverResourcesHeader;
    }

    public int getFlagID() {
        return flagID;
    }

    public void setFlagID(int flagID) {
        this.flagID = flagID;
    }
}
