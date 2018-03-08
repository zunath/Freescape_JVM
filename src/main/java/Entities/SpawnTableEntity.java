package Entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SpawnTables")
public class SpawnTableEntity {

    @Id
    @Column(name = "SpawnTableID")
    private int spawnTableID;

    @Column(name = "Name")
    private String name;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spawnTable", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SpawnTableCreatureEntity> spawnTableCreatures;

    public int getSpawnTableID() {
        return spawnTableID;
    }

    public void setSpawnTableID(int spawnTableID) {
        this.spawnTableID = spawnTableID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpawnTableCreatureEntity> getSpawnTableCreatures() {
        return spawnTableCreatures;
    }

    public void setSpawnTableCreatures(List<SpawnTableCreatureEntity> spawnTableCreatures) {
        this.spawnTableCreatures = spawnTableCreatures;
    }
}
