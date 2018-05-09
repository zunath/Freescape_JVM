package Entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PCTerritoryFlagsStructures")
public class PCTerritoryFlagStructureEntity {

    @Id
    @Column(name = "PCTerritoryFlagStructureID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcTerritoryFlagStructureID;

    @Column(name = "LocationAreaTag")
    private String locationAreaTag;

    @Column(name = "LocationX")
    private double locationX;

    @Column(name = "LocationY")
    private double locationY;

    @Column(name = "LocationZ")
    private double locationZ;

    @Column(name = "LocationOrientation")
    private double locationOrientation;

    @Column(name = "IsUseable")
    private boolean isUseable;

    @Column(name = "CreateDate", insertable = false)
    private Date createDate;

    @Column(name = "CustomName")
    private String customName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StructureBlueprintID")
    private StructureBlueprintEntity blueprint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PCTerritoryFlagID")
    private PCTerritoryFlagEntity pcTerritoryFlag;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "structure", fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<PCTerritoryFlagStructureItemEntity> items;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BuildingInteriorID")
    private BuildingInteriorEntity buildingInterior;

    public int getPcTerritoryFlagStructureID() {
        return pcTerritoryFlagStructureID;
    }

    public void setPcTerritoryFlagStructureID(int pcTerritoryFlagStructureID) {
        this.pcTerritoryFlagStructureID = pcTerritoryFlagStructureID;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public PCTerritoryFlagEntity getPcTerritoryFlag() {
        return pcTerritoryFlag;
    }

    public void setPcTerritoryFlag(PCTerritoryFlagEntity pcTerritoryFlag) {
        this.pcTerritoryFlag = pcTerritoryFlag;
    }

    public String getLocationAreaTag() {
        return locationAreaTag;
    }

    public void setLocationAreaTag(String locationAreaTag) {
        this.locationAreaTag = locationAreaTag;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public double getLocationZ() {
        return locationZ;
    }

    public void setLocationZ(double locationZ) {
        this.locationZ = locationZ;
    }

    public double getLocationOrientation() {
        return locationOrientation;
    }

    public void setLocationOrientation(double locationOrientation) {
        this.locationOrientation = locationOrientation;
    }

    public boolean isUseable() {
        return isUseable;
    }

    public void setIsUseable(boolean isUseable) {
        this.isUseable = isUseable;
    }

    public StructureBlueprintEntity getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(StructureBlueprintEntity blueprint) {
        this.blueprint = blueprint;
    }

    public List<PCTerritoryFlagStructureItemEntity> getItems() {
        return items;
    }

    public void setItems(List<PCTerritoryFlagStructureItemEntity> items) {
        this.items = items;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public BuildingInteriorEntity getBuildingInterior() {
        return buildingInterior;
    }

    public void setBuildingInterior(BuildingInteriorEntity buildingInterior) {
        this.buildingInterior = buildingInterior;
    }
}
