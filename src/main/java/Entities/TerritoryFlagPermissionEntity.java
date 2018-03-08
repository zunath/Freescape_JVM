package Entities;

import javax.persistence.*;

@Entity
@Table(name = "TerritoryFlagPermissions")
public class TerritoryFlagPermissionEntity {

    @Id
    @Column(name = "TerritoryFlagPermissionID")
    private int territoryFlagPermissionID;

    @Column(name = "Name")
    private String name;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "IsSelectable")
    private boolean isSelectable;

    public int getTerritoryFlagPermissionID() {
        return territoryFlagPermissionID;
    }

    public void setTerritoryFlagPermissionID(int territoryFlagPermissionID) {
        this.territoryFlagPermissionID = territoryFlagPermissionID;
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

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setIsSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }
}
