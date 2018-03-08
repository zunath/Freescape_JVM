package Entities;

import javax.persistence.*;

@Entity
@Table(name="KeyItemCategories")
@SuppressWarnings("UnusedDeclaration")
public class KeyItemCategoryEntity {

    @Id
    @Column(name="KeyItemCategoryID")
    private int keyItemCategoryID;

    @Column(name="Name")
    private String name;

    @Column(name="IsActive")
    private int isActive;

    public int getKeyItemCategoryID() {
        return keyItemCategoryID;
    }

    public void setKeyItemCategoryID(int keyItemCategoryID) {
        this.keyItemCategoryID = keyItemCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

}
