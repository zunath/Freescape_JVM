package Entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "ItemStorageCount")
    private int itemStorageCount;

    @Column(name = "VanityCount")
    private int vanityCount;

    @Column(name = "MaxBuildDistance")
    private double maxBuildDistance;

    @Column(name = "Level")
    private int level;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PerkID")
    private PerkEntity perk;

    @Column(name = "RequiredPerkLevel")
    private int requiredPerkLevel;

    @Column(name = "GivesSkillXP")
    private boolean givesSkillXP;

    @Column(name = "SpecialCount")
    private int specialCount;

    @Column(name = "IsVanity")
    private boolean isVanity;

    @Column(name = "IsSpecial")
    private boolean isSpecial;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blueprint", fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<StructureComponentEntity> components;

    @Column(name = "CraftTierLevel")
    private int craftTierLevel;

    @Column(name = "ResourceCount")
    private int resourceCount;

    @Column(name = "BuildingCount")
    private int buildingCount;

    @Column(name = "IsResource")
    private boolean isResource;

    @Column(name = "IsBuilding")
    private boolean isBuilding;

    @Column(name = "ResourceResref")
    private String resourceResref;

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

    public int getStructureCategoryID() {
        return structureCategoryID;
    }

    public void setStructureCategoryID(int structureCategoryID) {
        this.structureCategoryID = structureCategoryID;
    }

    public int getVanityCount() {
        return vanityCount;
    }

    public void setVanityCount(int vanityCount) {
        this.vanityCount = vanityCount;
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

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setTerritoryFlag(boolean territoryFlag) {
        isTerritoryFlag = territoryFlag;
    }

    public void setUseable(boolean useable) {
        isUseable = useable;
    }

    public List<StructureComponentEntity> getComponents() {
        return components;
    }

    public void setComponents(List<StructureComponentEntity> components) {
        this.components = components;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public PerkEntity getPerk() {
        return perk;
    }

    public void setPerk(PerkEntity perk) {
        this.perk = perk;
    }

    public int getRequiredPerkLevel() {
        return requiredPerkLevel;
    }

    public void setRequiredPerkLevel(int requiredPerkLevel) {
        this.requiredPerkLevel = requiredPerkLevel;
    }

    public boolean givesSkillXP() {
        return givesSkillXP;
    }

    public int getSpecialCount() {
        return specialCount;
    }

    public void setSpecialCount(int specialCount) {
        this.specialCount = specialCount;
    }

    public boolean isVanity() {
        return isVanity;
    }

    public void setVanity(boolean vanity) {
        isVanity = vanity;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public int getCraftTierLevel() {
        return craftTierLevel;
    }

    public void setCraftTierLevel(int craftTierLevel) {
        this.craftTierLevel = craftTierLevel;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getBuildingCount() {
        return buildingCount;
    }

    public void setBuildingCount(int buildingCount) {
        this.buildingCount = buildingCount;
    }

    public boolean isResource() {
        return isResource;
    }

    public void setResource(boolean resource) {
        isResource = resource;
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public void setBuilding(boolean building) {
        isBuilding = building;
    }

    public String getResourceResref() {
        return resourceResref;
    }

    public void setResourceResref(String resourceResref) {
        this.resourceResref = resourceResref;
    }
}
