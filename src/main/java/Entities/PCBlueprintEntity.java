package Entities;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "PCBlueprints")
public class PCBlueprintEntity {

    @Id
    @Column(name = "PCBlueprintID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcBlueprintID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "AcquiredDate")
    private Timestamp acquiredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CraftBlueprintID")
    private CraftBlueprintEntity blueprint;

    public int getPcBlueprintID() {
        return pcBlueprintID;
    }

    public void setPcBlueprintID(int pcBlueprintID) {
        this.pcBlueprintID = pcBlueprintID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public Timestamp getAcquiredDate() {
        return acquiredDate;
    }

    public void setAcquiredDate(Timestamp acquiredDate) {
        this.acquiredDate = acquiredDate;
    }

    public CraftBlueprintEntity getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(CraftBlueprintEntity blueprint) {
        this.blueprint = blueprint;
    }

}
