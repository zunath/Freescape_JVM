package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="KeyItems")
public class KeyItemEntity {

    @Id
    @Column(name="KeyItemID")
    private int keyItemID;

    @Column(name="KeyItemCategoryID")
    private int keyItemCategoryID;

    @Column(name="Name")
    private String Name;

    @Column(name="Description")
    private String Description;


    public int getKeyItemID() {
        return keyItemID;
    }

    public void setKeyItemID(int keyItemID) {
        this.keyItemID = keyItemID;
    }

    public int getKeyItemCategoryID() {
        return keyItemCategoryID;
    }

    public void setKeyItemCategoryID(int keyItemCategoryID) {
        this.keyItemCategoryID = keyItemCategoryID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
