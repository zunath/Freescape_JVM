package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PerkCategories")
public class PerkCategoryEntity {

    @Id
    @Column(name = "PerkCategoryID")
    private int perkCategoryID;

    @Column(name = "Name")
    private String name;

    @Column(name = "IsActive")
    private boolean isActive;

    public int getPerkCategoryID() {
        return perkCategoryID;
    }

    public void setPerkCategoryID(int perkCategoryID) {
        this.perkCategoryID = perkCategoryID;
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
