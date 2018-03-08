package Entities;


import javax.persistence.*;

@Entity
@Table(name = "PCTerritoryFlagsStructuresItems")
public class PCTerritoryFlagStructureItemEntity {

    @Id
    @Column(name = "PCStructureItemID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcStructureItemID;

    @Column(name = "ItemObject")
    private byte[] itemObject;

    @Column(name = "ItemName")
    private String itemName;

    @Column(name = "ItemTag")
    private String itemTag;

    @Column(name = "ItemResref")
    private String itemResref;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PCStructureID")
    private PCTerritoryFlagStructureEntity structure;

    public int getPcStructureItemID() {
        return pcStructureItemID;
    }

    public void setPcStructureItemID(int pcStructureItemID) {
        this.pcStructureItemID = pcStructureItemID;
    }

    public byte[] getItemObject() {
        return itemObject;
    }

    public void setItemObject(byte[] itemObject) {
        this.itemObject = itemObject;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemTag() {
        return itemTag;
    }

    public void setItemTag(String itemTag) {
        this.itemTag = itemTag;
    }

    public String getItemResref() {
        return itemResref;
    }

    public void setItemResref(String itemResref) {
        this.itemResref = itemResref;
    }

    public PCTerritoryFlagStructureEntity getStructure() {
        return structure;
    }

    public void setStructure(PCTerritoryFlagStructureEntity structure) {
        this.structure = structure;
    }
}
