package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="PCPerks")
public class PCPerksEntity {

    @Id
    @Column(name = "PCPerkID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcPerkID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "AcquiredDate")
    private Timestamp acquiredDate;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "PerkID")
    private PerkEntity perk;

    public int getPcBlueprintID() {
        return pcPerkID;
    }

    public void setPcBlueprintID(int pcAbilityID) {
        this.pcPerkID = pcAbilityID;
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

    public PerkEntity getPerk() {
        return perk;
    }

    public void setPerk(PerkEntity perk) {
        this.perk = perk;
    }
}
