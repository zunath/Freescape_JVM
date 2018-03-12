package Entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PCCooldowns")
public class PCCooldownEntity {

    @Id
    @Column(name = "PCCooldownID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcCooldownID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "CooldownCategoryID")
    private int cooldownCategoryID;

    @Column(name = "DateUnlocked")
    private Date dateUnlocked;

    public int getPCCooldownID() {
        return pcCooldownID;
    }

    public void setPCCooldownID(int pcCooldownID) {
        this.pcCooldownID = pcCooldownID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public int getCooldownCategoryID() {
        return cooldownCategoryID;
    }

    public void setCooldownCategoryID(int cooldownCategoryID) {
        this.cooldownCategoryID = cooldownCategoryID;
    }

    public Date getDateUnlocked() {
        return dateUnlocked;
    }

    public void setDateUnlocked(Date dateUnlocked) {
        this.dateUnlocked = dateUnlocked;
    }
}
