package Entities;

import javax.persistence.*;

@Entity
@Table(name = "SkillCategories")
public class SkillCategoryEntity {

    @Id
    @Column(name = "SkillCategoryID")
    private int skillCategoryID;

    @Column(name = "Name")
    private String name;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "Sequence")
    private int sequence;

    public int getSkillCategoryID() {
        return skillCategoryID;
    }

    public void setSkillCategoryID(int skillCategoryID) {
        this.skillCategoryID = skillCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
