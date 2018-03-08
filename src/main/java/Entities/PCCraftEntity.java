package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PCCrafts")
public class PCCraftEntity {

    @Id
    @Column(name = "PCCraftID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcCraftID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "CraftID")
    private int craftID;

    @Column(name = "Level")
    private int level;

    @Column(name = "Experience")
    private int experience;

    public int getPcCraftID() {
        return pcCraftID;
    }

    public void setPcCraftID(int pcCraftID) {
        this.pcCraftID = pcCraftID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public int getCraftID() {
        return craftID;
    }

    public void setCraftID(int craftID) {
        this.craftID = craftID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
