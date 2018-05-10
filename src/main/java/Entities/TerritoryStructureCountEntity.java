package Entities;

import javax.persistence.*;

@Entity
@SqlResultSetMapping(name="TerritoryStructureCountResult", classes = {
        @ConstructorResult(targetClass = TerritoryStructureCountEntity.class,
                columns = {
                    @ColumnResult(name="ID"),
                    @ColumnResult(name="VanityCount"),
                        @ColumnResult(name="SpecialCount"),
                        @ColumnResult(name="ResourceCount"),
                        @ColumnResult(name="BuildingCount")
                })
})
public class TerritoryStructureCountEntity {

    @Id
    private int id;
    private int vanityCount;
    private int specialCount;
    private int resourceCount;
    private int buildingCount;

    public TerritoryStructureCountEntity(int id, int vanityCount, int specialCount, int resourceCount, int buildingCount)
    {
        this.id = id;
        this.vanityCount = vanityCount;
        this.specialCount = specialCount;
        this.resourceCount = resourceCount;
        this.buildingCount = buildingCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVanityCount() {
        return vanityCount;
    }

    public void setVanityCount(int vanityCount) {
        this.vanityCount = vanityCount;
    }

    public int getSpecialCount() {
        return specialCount;
    }

    public void setSpecialCount(int specialCount) {
        this.specialCount = specialCount;
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
}
