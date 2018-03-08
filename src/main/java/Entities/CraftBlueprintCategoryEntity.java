package Entities;

import javax.persistence.*;

@Entity
@Table(name = "CraftBlueprintCategories")
public class CraftBlueprintCategoryEntity {

    @Id
    @Column(name = "CraftBlueprintCategoryID")
    private int craftBlueprintCategoryID;

    @Column(name = "Name")
    private String name;

    @Column(name = "IsActive")
    private boolean isActive;

    public int getCraftBlueprintCategoryID() {
        return craftBlueprintCategoryID;
    }

    public void setCraftBlueprintCategoryID(int craftBlueprintCategoryID) {
        this.craftBlueprintCategoryID = craftBlueprintCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
