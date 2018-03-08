package Entities;

import javax.persistence.*;

@Entity
@Table(name = "AbilityCategories")
public class AbilityCategoryEntity {

    @Id
    @Column(name = "AbilityCategoryID")
    private int abilityCategoryID;

    @Column(name = "Name")
    private String name;

    @Column(name = "IsActive")
    private boolean isActive;

    public int getAbilityCategoryID() {
        return abilityCategoryID;
    }

    public void setAbilityCategoryID(int abilityCategoryID) {
        this.abilityCategoryID = abilityCategoryID;
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
}
