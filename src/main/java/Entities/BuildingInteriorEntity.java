package Entities;

import javax.persistence.*;

@Entity
@Table(name = "BuildingInteriors")
public class BuildingInteriorEntity {

    @Id
    @Column(name = "BuildingInteriorID")
    private int buildingInteriorID;

    @Column(name = "BuildingCategoryID")
    private int buildingCategoryID;

    @Column(name = "AreaResref")
    private String areaResref;

    @Column(name = "Name")
    private String name;

    @Column(name = "IsDefaultForCategory")
    private boolean isDefaultForCategory;

    public int getBuildingInteriorID() {
        return buildingInteriorID;
    }

    public void setBuildingInteriorID(int buildingInteriorID) {
        this.buildingInteriorID = buildingInteriorID;
    }

    public int getBuildingCategoryID() {
        return buildingCategoryID;
    }

    public void setBuildingCategoryID(int buildingCategoryID) {
        this.buildingCategoryID = buildingCategoryID;
    }

    public String getAreaResref() {
        return areaResref;
    }

    public void setAreaResref(String areaResref) {
        this.areaResref = areaResref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefaultForCategory() {
        return isDefaultForCategory;
    }

    public void setDefaultForCategory(boolean defaultForCategory) {
        isDefaultForCategory = defaultForCategory;
    }
}
