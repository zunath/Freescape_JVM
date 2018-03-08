package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "StorageItems")
public class StorageItemEntity {

    @Id
    @Column(name ="StorageItemID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storageItemID;

    @Column(name = "ItemName")
    private String itemName;
    @Column(name = "ItemTag")
    private String itemTag;
    @Column(name = "ItemResref")
    private String itemResref;
    @Column(name = "ItemObject")
    private byte[] itemObject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StorageContainerID")
    private StorageContainerEntity storageContainer;

    public int getStorageItemID() {
        return storageItemID;
    }

    public void setStorageItemID(int storageItemID) {
        this.storageItemID = storageItemID;
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

    public StorageContainerEntity getStorageContainer() {
        return storageContainer;
    }

    public void setStorageContainer(StorageContainerEntity storageContainer) {
        this.storageContainer = storageContainer;
    }

}
