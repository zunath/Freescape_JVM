package Entities;

import javax.persistence.*;

@Entity
@Table(name = "StructureCategories")
public class StructureCategoryEntity {

    @Id
    @Column(name = "StructureCategoryID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int structureCategoryID;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "IsTerritoryFlagCategory")
    private boolean isTerritoryFlagCategory;

    public int getStructureCategoryID() {
        return structureCategoryID;
    }

    public void setStructureCategoryID(int structureCategoryID) {
        this.structureCategoryID = structureCategoryID;
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

    public boolean isTerritoryFlagCategory() {
        return isTerritoryFlagCategory;
    }

    public void setIsTerritoryFlagCategory(boolean isTerritoryFlagCategory) {
        this.isTerritoryFlagCategory = isTerritoryFlagCategory;
    }
}
