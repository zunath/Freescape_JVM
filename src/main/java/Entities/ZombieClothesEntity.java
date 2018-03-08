package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ZombieClothes")
public class ZombieClothesEntity {

    @Id
    @Column(name = "ZombieClothesID")
    private int zombieClothesID;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "IsActive")
    private boolean isActive;

    public int getZombieClothesID() {
        return zombieClothesID;
    }

    public void setZombieClothesID(int zombieClothesID) {
        this.zombieClothesID = zombieClothesID;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
