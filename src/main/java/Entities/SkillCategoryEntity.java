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
}
