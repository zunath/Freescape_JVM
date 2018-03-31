package GameSystems.Models;


public class SpawnModel {

    private String resref;

    private int lootTableID;

    public SpawnModel(String resref, int lootTableID)
    {
        this.resref = resref;
        this.lootTableID = lootTableID;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public int getLootTableID() {
        return lootTableID;
    }

    public void setLootTableID(int lootTableID) {
        this.lootTableID = lootTableID;
    }
}
