package Entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="GrowingPlants")
public class GrowingPlantEntity {

    @Id
    @Column(name = "GrowingPlantID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int growingPlantID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PlantID")
    private PlantEntity plant;

    @Column(name = "RemainingTicks")
    private int remainingTicks;

    @Column(name = "LocationAreaTag")
    private String locationAreaTag;

    @Column(name = "LocationX")
    private float locationX;

    @Column(name = "LocationY")
    private float locationY;

    @Column(name = "LocationZ")
    private float locationZ;

    @Column(name = "LocationOrientation")
    private float locationOrientation;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "DateCreated")
    private Date dateCreated;

    public int getGrowingPlantID() {
        return growingPlantID;
    }

    public void setGrowingPlantID(int growingPlantID) {
        this.growingPlantID = growingPlantID;
    }

    public PlantEntity getPlant() {
        return plant;
    }

    public void setPlant(PlantEntity plant) {
        this.plant = plant;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    public String getLocationAreaTag() {
        return locationAreaTag;
    }

    public void setLocationAreaTag(String locationAreaTag) {
        this.locationAreaTag = locationAreaTag;
    }

    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public float getLocationZ() {
        return locationZ;
    }

    public void setLocationZ(float locationZ) {
        this.locationZ = locationZ;
    }

    public float getLocationOrientation() {
        return locationOrientation;
    }

    public void setLocationOrientation(float locationOrientation) {
        this.locationOrientation = locationOrientation;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
