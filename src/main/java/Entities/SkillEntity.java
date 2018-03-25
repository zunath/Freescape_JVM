package Entities;

import javax.persistence.*;

@Entity
@Table(name = "Skills")
public class SkillEntity {

    @Id
    @Column(name = "SkillID")
    private int skillID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SkillCategoryID")
    private SkillCategoryEntity skillCategory;

    @Column(name = "Name")
    private String name;

    @Column(name = "MaxRank")
    private int maxRank;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "Description")
    private String description;

    @Column(name = "[Primary]")
    private int primary;

    @Column(name = "[Secondary]")
    private int secondary;

    @Column(name = "[Tertiary]")
    private int tertiary;

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public SkillCategoryEntity getSkillCategory() {
        return skillCategory;
    }

    public void setSkillCategory(SkillCategoryEntity skillCategory) {
        this.skillCategory = skillCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxRank() {
        return maxRank;
    }

    public void setMaxRank(int maxRank) {
        this.maxRank = maxRank;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public int getSecondary() {
        return secondary;
    }

    public void setSecondary(int secondary) {
        this.secondary = secondary;
    }

    public int getTertiary() {
        return tertiary;
    }

    public void setTertiary(int tertiary) {
        this.tertiary = tertiary;
    }
}
