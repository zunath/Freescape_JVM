package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PCTerritoryFlagsPermissions")
public class PCTerritoryFlagPermissionEntity {

    @Id
    @Column(name = "PCTerritoryFlagPermissionID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcTerritoryFlagPermissionID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PlayerID")
    private PlayerEntity player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TerritoryFlagPermissionID")
    private TerritoryFlagPermissionEntity permission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PCTerritoryFlagID")
    private PCTerritoryFlagEntity pcTerritoryFlag;

    public int getPcTerritoryFlagPermissionID() {
        return pcTerritoryFlagPermissionID;
    }

    public void setPcTerritoryFlagPermissionID(int pcTerritoryFlagPermissionID) {
        this.pcTerritoryFlagPermissionID = pcTerritoryFlagPermissionID;
    }

    public PCTerritoryFlagEntity getPcTerritoryFlag() {
        return pcTerritoryFlag;
    }

    public void setPcTerritoryFlag(PCTerritoryFlagEntity pcTerritoryFlag) {
        this.pcTerritoryFlag = pcTerritoryFlag;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public TerritoryFlagPermissionEntity getPermission() {
        return permission;
    }

    public void setPermission(TerritoryFlagPermissionEntity permission) {
        this.permission = permission;
    }
}
