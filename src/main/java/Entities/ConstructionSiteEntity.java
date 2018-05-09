package Entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ConstructionSites")
public class ConstructionSiteEntity {

    @Id
    @Column(name = "ConstructionSiteID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int constructionSiteID;

    @Column(name = "PlayerID")
    private String playerID;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StructureBlueprintID")
    private StructureBlueprintEntity blueprint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PCTerritoryFlagID")
    private PCTerritoryFlagEntity pcTerritoryFlag;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constructionSite", fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ConstructionSiteComponentEntity> components;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BuildingInteriorID")
    private BuildingInteriorEntity buildingInterior;

    public int getConstructionSiteID() {
        return constructionSiteID;
    }

    public void setConstructionSiteID(int constructionSiteID) {
        this.constructionSiteID = constructionSiteID;
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

    public StructureBlueprintEntity getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(StructureBlueprintEntity blueprint) {
        this.blueprint = blueprint;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public PCTerritoryFlagEntity getPcTerritoryFlag() {
        return pcTerritoryFlag;
    }

    public void setPcTerritoryFlag(PCTerritoryFlagEntity pcTerritoryFlag) {
        this.pcTerritoryFlag = pcTerritoryFlag;
    }

    public List<ConstructionSiteComponentEntity> getComponents() {
        return components;
    }

    public void setComponents(List<ConstructionSiteComponentEntity> components) {
        this.components = components;
    }

    public BuildingInteriorEntity getBuildingInterior() {
        return buildingInterior;
    }

    public void setBuildingInterior(BuildingInteriorEntity buildingInterior) {
        this.buildingInterior = buildingInterior;
    }
}
