package Conversation.ViewModels;

import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.ArrayList;
import java.util.List;

public class BuildToolMenuModel {

    private List<NWObject> nearbyStructures;
    private NWObject activeStructure;
    private boolean isConfirmingRaze;
    private NWLocation targetLocation;
    private NWObject flag;

    public BuildToolMenuModel()
    {
        nearbyStructures = new ArrayList<>();
        isConfirmingRaze = false;
    }

    public List<NWObject> getNearbyStructures() {
        return nearbyStructures;
    }

    public void setNearbyStructures(List<NWObject> nearbyStructures) {
        this.nearbyStructures = nearbyStructures;
    }

    public NWObject getActiveStructure() {
        return activeStructure;
    }

    public void setActiveStructure(NWObject activeStructure) {
        this.activeStructure = activeStructure;
    }

    public boolean isConfirmingRaze() {
        return isConfirmingRaze;
    }

    public void setIsConfirmingRaze(boolean isConfirmingRaze) {
        this.isConfirmingRaze = isConfirmingRaze;
    }

    public NWLocation getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(NWLocation targetLocation) {
        this.targetLocation = targetLocation;
    }

    public NWObject getFlag() {
        return flag;
    }

    public void setFlag(NWObject flag) {
        this.flag = flag;
    }
}
