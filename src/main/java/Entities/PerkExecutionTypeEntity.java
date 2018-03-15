package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PerkExecutionTypes")
public class PerkExecutionTypeEntity {

    @Id
    @Column(name = "PerkExecutionTypeID")
    private int perkExecutionTypeID;

    @Column(name = "Name")
    private String name;

    public int getPerkExecutionTypeID() {
        return perkExecutionTypeID;
    }

    public void setPerkExecutionTypeID(int perkExecutionTypeID) {
        this.perkExecutionTypeID = perkExecutionTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
