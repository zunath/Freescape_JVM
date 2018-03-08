package Entities;

import javax.persistence.*;

@Entity
@Table(name = "StructureBlueprints")
public class StructureBlueprintEntity {

    @Id
    @Column(name = "StructureBlueprintID")
    private int structureBlueprintID;

    @Column(name = "StructureCategoryID")
    private int structureCategoryID;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "IsTerritoryFlag")
    private boolean isTerritoryFlag;

    @Column(name = "IsUseable")
    private boolean isUseable;

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

    @Column(name = "ItemStorageCount")
    private int itemStorageCount;

    @Column(name = "MaxStructuresCount")
    private int maxStructuresCount;

    @Column(name = "MaxBuildDistance")
    private double maxBuildDistance;

    @Column(name = "ResearchSlots")
    private int researchSlots;

    @Column(name = "RPPerSecond")
    private int rpPerSecond;

    public int getStructureBlueprintID() {
        return structureBlueprintID;
    }

    public void setStructureBlueprintID(int blueprintStructureID) {
        this.structureBlueprintID = blueprintStructureID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    public int getItemStorageCount() {
        return itemStorageCount;
    }

    public void setItemStorageCount(int itemStorageCount) {
        this.itemStorageCount = itemStorageCount;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public int getMetalRequired() {
        return metalRequired;
    }

    public void setMetalRequired(int metalRequired) {
        this.metalRequired = metalRequired;
    }

    public int getStructureCategoryID() {
        return structureCategoryID;
    }

    public void setStructureCategoryID(int structureCategoryID) {
        this.structureCategoryID = structureCategoryID;
    }

    public int getMaxStructuresCount() {
        return maxStructuresCount;
    }

    public void setMaxStructuresCount(int maxStructuresCount) {
        this.maxStructuresCount = maxStructuresCount;
    }

    public double getMaxBuildDistance() {
        return maxBuildDistance;
    }

    public void setMaxBuildDistance(double maxBuildDistance) {
        this.maxBuildDistance = maxBuildDistance;
    }

    public boolean isTerritoryFlag() {
        return isTerritoryFlag;
    }

    public void setIsTerritoryFlag(boolean isTerritoryFlag) {
        this.isTerritoryFlag = isTerritoryFlag;
    }

    public boolean isUseable() {
        return isUseable;
    }

    public void setIsUseable(boolean isUseable) {
        this.isUseable = isUseable;
    }

    public int getIronRequired() {
        return ironRequired;
    }

    public void setIronRequired(int ironRequired) {
        this.ironRequired = ironRequired;
    }

    public int getResearchSlots() {
        return researchSlots;
    }

    public void setResearchSlots(int researchSlots) {
        this.researchSlots = researchSlots;
    }

    public int getRpPerSecond() {
        return rpPerSecond;
    }

    public void setRpPerSecond(int rpPerSecond) {
        this.rpPerSecond = rpPerSecond;
    }
}
