package Conversation.ViewModels;

public class TerritoryFlagMenuModel {

    private int flagID;
    private boolean isConfirmingTerritoryRaze;
    private String transferUUID;
    private boolean isConfirmingTransferTerritory;
    private String activePermissionsUUID;
    private boolean isAddingPermission;

    public int getFlagID() {
        return flagID;
    }

    public void setFlagID(int flagID) {
        this.flagID = flagID;
    }

    public boolean isConfirmingTerritoryRaze() {
        return isConfirmingTerritoryRaze;
    }

    public void setIsConfirmingTerritoryRaze(boolean isConfirmingTerritoryRaze) {
        this.isConfirmingTerritoryRaze = isConfirmingTerritoryRaze;
    }

    public String getTransferUUID() {
        return transferUUID;
    }

    public void setTransferUUID(String transferUUID) {
        this.transferUUID = transferUUID;
    }

    public boolean isConfirmingTransferTerritory() {
        return isConfirmingTransferTerritory;
    }

    public void setIsConfirmingTransferTerritory(boolean isConfirmingTransferTerritory) {
        this.isConfirmingTransferTerritory = isConfirmingTransferTerritory;
    }

    public String getActivePermissionsUUID() {
        return activePermissionsUUID;
    }

    public void setActivePermissionsUUID(String activePermissionsUUID) {
        this.activePermissionsUUID = activePermissionsUUID;
    }

    public boolean isAddingPermission() {
        return isAddingPermission;
    }

    public void setIsAddingPermission(boolean isAddingPermission) {
        this.isAddingPermission = isAddingPermission;
    }
}
