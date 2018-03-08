package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BaseItemTypes")
public class BaseItemTypeEntity {

    @Id
    @Column(name = "BaseItemTypeID")
    private int baseItemTypeID;

    @Column(name = "Name")
    private String name;

    public int getBaseItemTypeID() {
        return baseItemTypeID;
    }

    public void setBaseItemTypeID(int baseItemTypeID) {
        this.baseItemTypeID = baseItemTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
