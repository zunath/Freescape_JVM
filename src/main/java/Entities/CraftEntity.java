package Entities;

import javax.persistence.*;

@Entity
@Table(name = "Crafts")
public class CraftEntity {

    @Id
    @Column(name = "CraftID")
    private int craftID;

    @Column(name = "Name")
    private String name;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "Description")
    private String description;

    public int getCraftID() {
        return craftID;
    }

    public void setCraftID(int craftID) {
        this.craftID = craftID;
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

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
