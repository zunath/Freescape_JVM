package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "StructureQuickBuildAudit")
public class StructureQuickBuildAuditEntity {

    @Column(name="StructureQuickBuildID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int structureQuickBuildID;

    @Column(name = "PCTerritoryFlagID")
    private Integer pcTerritoryFlagID;

    @Column(name = "PCTerritoryFlagStructureID")
    private Long pcTerritoryFlagStructureID;

    @Column(name = "DMName")
    private String dmName;

    @Column(name = "DateBuilt")
    private Timestamp dateBuilt;

    public int getStructureQuickBuildID() {
        return structureQuickBuildID;
    }

    public void setStructureQuickBuildID(int structureQuickBuildID) {
        this.structureQuickBuildID = structureQuickBuildID;
    }

    public Integer getPcTerritoryFlagID() {
        return pcTerritoryFlagID;
    }

    public void setPcTerritoryFlagID(Integer pcTerritoryFlagID) {
        this.pcTerritoryFlagID = pcTerritoryFlagID;
    }

    public Long getPcTerritoryFlagStructureID() {
        return pcTerritoryFlagStructureID;
    }

    public void setPcTerritoryFlagStructureID(Long pcTerritoryFlagStructureID) {
        this.pcTerritoryFlagStructureID = pcTerritoryFlagStructureID;
    }

    public String getDmName() {
        return dmName;
    }

    public void setDmName(String dmName) {
        this.dmName = dmName;
    }

    public Timestamp getDateBuilt() {
        return dateBuilt;
    }

    public void setDateBuilt(Timestamp dateBuilt) {
        this.dateBuilt = dateBuilt;
    }
}
