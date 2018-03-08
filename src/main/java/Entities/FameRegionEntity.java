package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="FameRegions")
public class FameRegionEntity {


    @Id
    @Column(name="FameRegionID")
    private int fameRegionID;

    @Column(name="Name")
    private String name;

    public int getFameRegionID() {
        return fameRegionID;
    }

    public void setFameRegionID(int fameRegionID) {
        this.fameRegionID = fameRegionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
