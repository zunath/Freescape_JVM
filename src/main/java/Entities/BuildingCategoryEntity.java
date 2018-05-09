package Entities;

import javax.persistence.*;

@Entity
@Table(name = "BuildingCategories")
public class BuildingCategoryEntity {

    @Id
    @Column(name = "BuildingCategoryID")
    private int buildingCategoryID;

    @Column(name = "Name")
    private String name;

    public int getBuildingCategoryID() {
        return buildingCategoryID;
    }

    public void setBuildingCategoryID(int buildingCategoryID) {
        this.buildingCategoryID = buildingCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
