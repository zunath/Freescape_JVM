package Entities;

import javax.persistence.*;

@Entity
@Table(name="Plants")
public class PlantEntity  {

    @Id
    @Column(name = "PlantID")
    private int plantID;

    @Column(name = "Name")
    private String name;

    @Column(name = "BaseTicks")
    private int baseTicks;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "WaterTicks")
    private int waterTicks;

    @Column(name = "Level")
    private int level;

    public int getPlantID() {
        return plantID;
    }

    public void setPlantID(int plantID) {
        this.plantID = plantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBaseTicks() {
        return baseTicks;
    }

    public void setBaseTicks(int baseTicks) {
        this.baseTicks = baseTicks;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public int getWaterTicks() {
        return waterTicks;
    }

    public void setWaterTicks(int waterTicks) {
        this.waterTicks = waterTicks;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
