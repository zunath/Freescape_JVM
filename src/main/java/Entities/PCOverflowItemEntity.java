package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PCOverflowItems")
public class PCOverflowItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PCOverflowItemID")
    private int pcOverflowItemID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "ItemName")
    private String itemName;

    @Column(name = "ItemTag")
    private String itemTag;

    @Column(name = "ItemResref")
    private String itemResref;

    @Column(name = "ItemObject")
    private byte[] itemObject;


    public int getPcOverflowItemID() {
        return pcOverflowItemID;
    }

    public void setPcOverflowItemID(int pcOverflowItemID) {
        this.pcOverflowItemID = pcOverflowItemID;
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

    public byte[] getItemObject() {
        return itemObject;
    }

    public void setItemObject(byte[] itemObject) {
        this.itemObject = itemObject;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }
}
