package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="PlayerProgressionSkills")
public class PlayerProgressionSkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PlayerProgressionSkillID")
    private int playerProgressionSkillID;

    @Column(name="PlayerID")
    private String pcID;

    @Column(name="ProgressionSkillID")
    private int progressionSkillID;

    @Column(name = "UpgradeLevel")
    private int upgradeLevel;

    @Column(name = "IsSoftCapUnlocked")
    private boolean isSoftCapUnlocked;

    public int getPlayerProgressionSkillID() {
        return playerProgressionSkillID;
    }

    public void setPlayerProgressionSkillID(int playerProgressionSkillID) {
        this.playerProgressionSkillID = playerProgressionSkillID;
    }

    public String getPcID() {
        return pcID;
    }

    public void setPcID(String pcID) {
        this.pcID = pcID;
    }

    public int getProgressionSkillID() {
        return progressionSkillID;
    }

    public void setProgressionSkillID(int progressionSkillID) {
        this.progressionSkillID = progressionSkillID;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(int upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public boolean isSoftCapUnlocked() {
        return isSoftCapUnlocked;
    }

    public void setIsSoftCapUnlocked(boolean isSoftCapUnlocked) {
        this.isSoftCapUnlocked = isSoftCapUnlocked;
    }
}
