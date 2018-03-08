package Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ActivePlayers")
public class ActivePlayerEntity {

    @Id
    @Column(name = "ActivePlayerID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int activePlayerID;

    @Column(name = "AccountName")
    private String accountName;

    @Column(name = "AreaName")
    private String areaName;

    @Column(name = "CharacterName")
    private String characterName;

    @Column(name ="ExpPercentage")
    private int expPercentage;

    @Column(name = "LevelPercentage")
    private int levelPercentage;

    @Column(name = "Level")
    private int level;

    @Column(name = "CreateDate", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "Description")
    private String description;

    public int getActivePlayerID() {
        return activePlayerID;
    }

    public void setActivePlayerID(int activePlayerID) {
        this.activePlayerID = activePlayerID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getExpPercentage() {
        return expPercentage;
    }

    public void setExpPercentage(int expPercentage) {
        this.expPercentage = expPercentage;
    }

    public int getLevelPercentage() {
        return levelPercentage;
    }

    public void setLevelPercentage(int levelPercentage) {
        this.levelPercentage = levelPercentage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
