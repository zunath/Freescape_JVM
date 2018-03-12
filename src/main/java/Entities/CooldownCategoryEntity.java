package Entities;

import javax.persistence.*;

@Entity
@Table(name = "CooldownCategories")
public class CooldownCategoryEntity {

    @Id
    @Column(name = "CooldownCategoryID")
    private int cooldownCategoryID;

    @Column(name = "Name")
    private String name;

    @Column(name = "BaseCooldownTime")
    private float BaseCooldownTime;

    public int getCooldownCategoryID() {
        return cooldownCategoryID;
    }

    public void setCooldownCategoryID(int cooldownCategoryID) {
        this.cooldownCategoryID = cooldownCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBaseCooldownTime() {
        return BaseCooldownTime;
    }

    public void setBaseCooldownTime(float baseCooldownTime) {
        BaseCooldownTime = baseCooldownTime;
    }
}
