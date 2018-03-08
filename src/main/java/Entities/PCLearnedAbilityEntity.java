package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="PCLearnedAbilities")
public class PCLearnedAbilityEntity {

    @Id
    @Column(name = "PCLearnedAbilityID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcAbilityID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "AcquiredDate")
    private Timestamp acquiredDate;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "AbilityID")
    private AbilityEntity ability;

    public int getPcBlueprintID() {
        return pcAbilityID;
    }

    public void setPcBlueprintID(int pcAbilityID) {
        this.pcAbilityID = pcAbilityID;
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

    public AbilityEntity getAbility() {
        return ability;
    }

    public void setAbility(AbilityEntity ability) {
        this.ability = ability;
    }
}
