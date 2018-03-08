package Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "StorageContainers")
public class StorageContainerEntity {

    @Id
    @Column(name = "StorageContainerID")
    private int storageContainerID;
    @Column(name = "AreaName")
    private String areaName;
    @Column(name = "AreaTag")
    private String areaTag;
    @Column(name = "AreaResref")
    private String areaResref;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storageContainer", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<StorageItemEntity> storageItems;

    public StorageContainerEntity()
    {
        storageContainerID = 0;
        areaName = "";
        areaTag = "";
        areaResref = "";
        storageItems = new ArrayList<>();
    }

    public int getStorageContainerID() {
        return storageContainerID;
    }

    public void setStorageContainerID(int storageContainerID) {
        this.storageContainerID = storageContainerID;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaTag() {
        return areaTag;
    }

    public void setAreaTag(String areaTag) {
        this.areaTag = areaTag;
    }

    public String getAreaResref() {
        return areaResref;
    }

    public void setAreaResref(String areaResref) {
        this.areaResref = areaResref;
    }

    public List<StorageItemEntity> getStorageItems() {
        return storageItems;
    }

    public void setStorageItems(List<StorageItemEntity> storageItems) {
        this.storageItems = storageItems;
    }
}
