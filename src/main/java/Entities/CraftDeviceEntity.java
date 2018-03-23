package Entities;

import javax.persistence.*;

@Entity
@Table(name = "CraftDevices")
public class CraftDeviceEntity {

    @Id
    @Column(name = "CraftDeviceID")
    private int craftDeviceID;

    @Column(name = "Name")
    private String name;

    public int getCraftDeviceID() {
        return craftDeviceID;
    }

    public void setCraftDeviceID(int craftDeviceID) {
        this.craftDeviceID = craftDeviceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
