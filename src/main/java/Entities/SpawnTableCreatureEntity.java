package Entities;


import javax.persistence.*;

@Entity
@Table(name = "SpawnTableCreatures")
public class SpawnTableCreatureEntity {

    @Id
    @Column(name = "SpawnTableCreatureID")
    private int spawnTableCreatureID;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "Weight")
    private int weight;

    @Column(name = "MaxNumberInArea")
    private int maxNumberInArea;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SpawnTableID")
    private SpawnTableEntity spawnTable;

    @Column(name = "LootTableID")
    private Integer lootTableID;

    @Column(name = "DifficultyRating")
    private int difficultyRating;


    public int getSpawnTableCreatureID() {
        return spawnTableCreatureID;
    }

    public void setSpawnTableCreatureID(int spawnTableCreatureID) {
        this.spawnTableCreatureID = spawnTableCreatureID;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMaxNumberInArea() {
        return maxNumberInArea;
    }

    public void setMaxNumberInArea(int maxNumberInArea) {
        this.maxNumberInArea = maxNumberInArea;
    }

    public SpawnTableEntity getSpawnTable() {
        return spawnTable;
    }

    public void setSpawnTable(SpawnTableEntity spawnTable) {
        this.spawnTable = spawnTable;
    }

    public Integer getLootTableID() {
        return lootTableID;
    }

    public void setLootTableID(Integer lootTableID) {
        this.lootTableID = lootTableID;
    }

    public int getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(int difficultyRating) {
        this.difficultyRating = difficultyRating;
    }
}
