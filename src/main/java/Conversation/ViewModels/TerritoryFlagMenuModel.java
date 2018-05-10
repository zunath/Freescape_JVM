package Conversation.ViewModels;

import org.nwnx.nwnx2.jvm.NWObject;

public class TerritoryFlagMenuModel {

    private NWObject flagMarker;
    private int flagID;
    private boolean isConfirmingTerritoryRaze;
    private String transferUUID;
    private boolean isConfirmingTransferTerritory;
    private String activePermissionsUUID;
    private boolean isAddingPermission;

    public NWObject getFlagMarker() {
        return flagMarker;
    }

    public void setFlagMarker(NWObject flagMarker) {
        this.flagMarker = flagMarker;
    }

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
