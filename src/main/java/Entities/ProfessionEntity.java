package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ProfessionsDomain")
public class ProfessionEntity {

    @Id
    @Column(name="ProfessionID")
    private int professionID;

    @Column(name="Name")
    private String name;

    @Column(name="Description")
    private String description;

    @Column(name="Bonuses")
    private String bonuses;

    @Column(name="IsActive")
    private boolean isActive;

    public int getProfessionID() {
        return professionID;
    }

    public void setProfessionID(int professionID) {
        this.professionID = professionID;
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

    public String getBonuses() {
        return bonuses;
    }

    public void setBonuses(String bonuses) {
        this.bonuses = bonuses;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
