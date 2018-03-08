package Entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PCAbilityCooldowns")
public class PCAbilityCooldownEntity {

    @Id
    @Column(name = "PCAbilityCooldownID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcAbilityCooldownID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "AbilityCooldownCategoryID")
    private int abilityCooldownCategoryID;

    @Column(name = "DateUnlocked")
    private Date dateUnlocked;

    public int getPcAbilityCooldownID() {
        return pcAbilityCooldownID;
    }

    public void setPcAbilityCooldownID(int pcAbilityCooldownID) {
        this.pcAbilityCooldownID = pcAbilityCooldownID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public int getAbilityCooldownCategoryID() {
        return abilityCooldownCategoryID;
    }

    public void setAbilityCooldownCategoryID(int abilityCooldownCategoryID) {
        this.abilityCooldownCategoryID = abilityCooldownCategoryID;
    }

    public Date getDateUnlocked() {
        return dateUnlocked;
    }

    public void setDateUnlocked(Date dateUnlocked) {
        this.dateUnlocked = dateUnlocked;
    }
}
