package GameSystems.Models;


public class SpawnModel {

    private String resref;

    private int lootTableID;

    private int difficultyRating;

    public SpawnModel(String resref, int lootTableID, int difficultyRating)
    {
        this.resref = resref;
        this.lootTableID = lootTableID;
        this.difficultyRating = difficultyRating;
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

    public int getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(int difficultyRating) {
        this.difficultyRating = difficultyRating;
    }
}
