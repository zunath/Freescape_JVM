package Entities;

import javax.persistence.*;

@Entity
@Table(name = "AbilityCooldownCategories")
public class AbilityCooldownCategoryEntity {

    @Id
    @Column(name = "AbilityCooldownCategoryID")
    private int abilityCooldownCategoryID;

    @Column(name = "Name")
    private String name;

    @Column(name = "BaseCooldownTime")
    private float BaseCooldownTime;

    public int getAbilityCooldownCategoryID() {
        return abilityCooldownCategoryID;
    }

    public void setAbilityCooldownCategoryID(int abilityCooldownCategoryID) {
        this.abilityCooldownCategoryID = abilityCooldownCategoryID;
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
