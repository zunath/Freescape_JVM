package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="ProgressionSkills")
public class ProgressionSkillEntity {

    @Id
    @Column(name="SkillID")
    private int skillID;
    @Column(name="Name")
    private String name;
    @Column(name="Description")
    private String description;
    @Column(name="MaxUpgrades")
    private int maxUpgrades;
    @Column(name="SoftCap")
    private int softCap;
    @Column(name="InitialPrice")
    private int initialPrice;
    @Column(name="IsFeat")
    private boolean isFeat;
    @Column(name="IsDisabled")
    private boolean isDisabled;


    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxUpgrades() {
        return maxUpgrades;
    }

    public void setMaxUpgrades(int maxUpgrades) {
        this.maxUpgrades = maxUpgrades;
    }

    public int getSoftCap() {
        return softCap;
    }

    public void setSoftCap(int softCap) {
        this.softCap = softCap;
    }

    public int getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(int initialPrice) {
        this.initialPrice = initialPrice;
    }

    public boolean isFeat() {
        return isFeat;
    }

    public void setFeat(boolean isFeat) {
        this.isFeat = isFeat;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
}
