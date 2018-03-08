package Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PCMigrations")
public class PCMigrationEntity {

    @Id
    @Column(name = "PCMigrationID")
    private int pcMigrationID;

    @Column(name = "Name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pcMigration", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PCMigrationItemEntity> pcMigrationItems;

    public PCMigrationEntity()
    {
        this.pcMigrationItems = new ArrayList<>();
    }


    public int getPcMigrationID() {
        return pcMigrationID;
    }

    public void setPcMigrationID(int pcMigrationID) {
        this.pcMigrationID = pcMigrationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PCMigrationItemEntity> getPcMigrationItems() {
        return pcMigrationItems;
    }

    public void setPcMigrationItems(List<PCMigrationItemEntity> pcMigrationItems) {
        this.pcMigrationItems = pcMigrationItems;
    }
}
