package Entities;

import javax.persistence.*;

@Entity
@Table(name = "CraftLevels")
public class CraftLevelEntity {

    @Id
    @Column(name = "CraftLevelID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int craftLevelID;

    @Column(name = "CraftID")
    private int craftID;

    @Column(name = "Level")
    private int level;

    @Column(name = "Experience")
    private int experience;


    public int getCraftLevelID() {
        return craftLevelID;
    }

    public void setCraftLevelID(int craftLevelID) {
        this.craftLevelID = craftLevelID;
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
