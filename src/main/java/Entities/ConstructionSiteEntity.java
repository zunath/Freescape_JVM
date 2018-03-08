package Entities;

import javax.persistence.*;

@Entity
@Table(name = "ConstructionSites")
public class ConstructionSiteEntity {

    @Id
    @Column(name = "ConstructionSiteID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int constructionSiteID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "WoodRequired")
    private int woodRequired;

    @Column(name = "MetalRequired")
    private int metalRequired;

    @Column(name = "NailsRequired")
    private int nailsRequired;

    @Column(name = "ClothRequired")
    private int clothRequired;

    @Column(name = "LeatherRequired")
    private int leatherRequired;

    @Column(name = "IronRequired")
    private int ironRequired;

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

    public int getConstructionSiteID() {
        return constructionSiteID;
    }

    public void setConstructionSiteID(int constructionSiteID) {
        this.constructionSiteID = constructionSiteID;
    }

    public int getWoodRequired() {
        return woodRequired;
    }

    public void setWoodRequired(int woodRequired) {
        this.woodRequired = woodRequired;
    }

    public int getNailsRequired() {
        return nailsRequired;
    }

    public void setNailsRequired(int nailsRequired) {
        this.nailsRequired = nailsRequired;
    }

    public int getClothRequired() {
        return clothRequired;
    }

    public void setClothRequired(int clothRequired) {
        this.clothRequired = clothRequired;
    }

    public int getLeatherRequired() {
        return leatherRequired;
    }

    public void setLeatherRequired(int leatherRequired) {
        this.leatherRequired = leatherRequired;
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

    public int getMetalRequired() {
        return metalRequired;
    }

    public void setMetalRequired(int metalRequired) {
        this.metalRequired = metalRequired;
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

    public int getIronRequired() {
        return ironRequired;
    }

    public void setIronRequired(int ironRequired) {
        this.ironRequired = ironRequired;
    }
}
