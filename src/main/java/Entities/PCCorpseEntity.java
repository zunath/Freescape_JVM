package Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="PCCorpses")
public class PCCorpseEntity {

    @Id
    @Column(name="PCCorpseID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcCorpseID;

    @Column(name="Name")
    private String name;

    @Column(name="PositionX")
    private float positionX;

    @Column(name="PositionY")
    private float positionY;

    @Column(name="PositionZ")
    private float positionZ;

    @Column(name="Orientation")
    private float orientation;

    @Column(name="AreaTag")
    private String areaTag;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corpse", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PCCorpseItemEntity> corpseItems;

    public PCCorpseEntity()
    {
        corpseItems = new ArrayList<>();
    }

    public int getPcCorpseID() {
        return pcCorpseID;
    }

    public void setPcCorpseID(int pcCorpseID) {
        this.pcCorpseID = pcCorpseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public float getPositionZ() {
        return positionZ;
    }

    public void setPositionZ(float positionZ) {
        this.positionZ = positionZ;
    }

    public float getOrientation() {
        return orientation;
    }

    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    public String getAreaTag() {
        return areaTag;
    }

    public void setAreaTag(String areaTag) {
        this.areaTag = areaTag;
    }

    public List<PCCorpseItemEntity> getCorpseItems() {
        return corpseItems;
    }

    public void setCorpseItems(List<PCCorpseItemEntity> corpseItems) {
        this.corpseItems = corpseItems;
    }

}
