package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PCMigrationItems")
public class PCMigrationItemEntity {

    @Id
    @Column(name = "PCMigrationItemID")
    private int pcMigrationItemID;

    @Column(name = "CurrentResref")
    private String currentResref;

    @Column(name = "NewResref")
    private String newResref;

    @Column(name = "StripItemProperties")
    private boolean stripItemProperties;

    @Column(name = "BaseItemTypeID")
    private int baseItemTypeID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PCMigrationID")
    private PCMigrationEntity pcMigration;

    public int getPcMigrationItemID() {
        return pcMigrationItemID;
    }

    public void setPcMigrationItemID(int pcMigrationItemID) {
        this.pcMigrationItemID = pcMigrationItemID;
    }

    public String getCurrentResref() {
        return currentResref;
    }

    public void setCurrentResref(String currentResref) {
        this.currentResref = currentResref;
    }

    public String getNewResref() {
        return newResref;
    }

    public void setNewResref(String newResref) {
        this.newResref = newResref;
    }

    public boolean isStripItemProperties() {
        return stripItemProperties;
    }

    public void setStripItemProperties(boolean stripItemProperties) {
        this.stripItemProperties = stripItemProperties;
    }

    public int getBaseItemTypeID() {
        return baseItemTypeID;
    }

    public void setBaseItemTypeID(int baseItemTypeID) {
        this.baseItemTypeID = baseItemTypeID;
    }

    public PCMigrationEntity getPcMigration() {
        return pcMigration;
    }

    public void setPcMigration(PCMigrationEntity pcMigration) {
        this.pcMigration = pcMigration;
    }
}
