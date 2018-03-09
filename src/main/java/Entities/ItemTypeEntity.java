package Entities;

import javax.persistence.*;

@Entity
@Table(name = "ItemTypes")
public class ItemTypeEntity {

    @Id
    @Column(name = "ItemTypeID")
    private int itemTypeID;

    @Column(name = "Name")
    private String name;

    public int getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(int itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
