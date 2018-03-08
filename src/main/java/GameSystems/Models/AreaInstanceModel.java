package GameSystems.Models;

public class AreaInstanceModel {
    private String areaResref;
    private int instanceCount;

    public AreaInstanceModel(String resref, int count)
    {
        areaResref = resref;
        instanceCount = count;
    }

    public String getAreaResref() {
        return areaResref;
    }

    public void setAreaResref(String areaResref) {
        this.areaResref = areaResref;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }
}
