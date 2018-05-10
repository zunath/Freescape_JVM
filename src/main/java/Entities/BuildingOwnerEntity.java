package Entities;

import javax.persistence.*;

@Entity
@SqlResultSetMapping(name="BuildingOwnerEntityResult", classes = {
        @ConstructorResult(targetClass = BuildingOwnerEntity.class,
                columns = {
                        @ColumnResult(name="ID"),
                        @ColumnResult(name="TerritoryOwner"),
                        @ColumnResult(name="BuildingOwner")})
})
public class BuildingOwnerEntity {

    @Id
    private int id;

    private String territoryOwner;

    private String buildingOwner;

    public BuildingOwnerEntity(int id, String territoryOwner, String buildingOwner)
    {
        this.id = id;
        this.territoryOwner = territoryOwner;
        this.buildingOwner = buildingOwner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerritoryOwner() {
        return territoryOwner;
    }

    public void setTerritoryOwner(String territoryOwner) {
        this.territoryOwner = territoryOwner;
    }

    public String getBuildingOwner() {
        return buildingOwner;
    }

    public void setBuildingOwner(String buildingOwner) {
        this.buildingOwner = buildingOwner;
    }
}
