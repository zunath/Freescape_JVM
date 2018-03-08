package Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PCSearchSites")
public class PCSearchSiteEntity implements Serializable {

    @Id
    @Column(name = "PCSearchSiteID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcSearchSiteID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "SearchSiteID")
    private int searchSiteID;

    @Column(name = "UnlockDateTime")
    private Timestamp unlockDateTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "searchSite", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PCSearchSiteItemEntity> searchItems;

    public PCSearchSiteEntity()
    {
        searchItems = new ArrayList<>();
    }

    public int getPcSearchSiteID() {
        return pcSearchSiteID;
    }

    public void setPcSearchSiteID(int pcSearchSiteID) {
        this.pcSearchSiteID = pcSearchSiteID;
    }

    public String getPcID() {
        return playerID;
    }

    public void setPcID(String playerID) {
        this.playerID = playerID;
    }

    public int getSearchSiteID() {
        return searchSiteID;
    }

    public void setSearchSiteID(int searchSiteID) {
        this.searchSiteID = searchSiteID;
    }

    public Timestamp getUnlockDateTime() {
        return unlockDateTime;
    }

    public void setUnlockDateTime(Timestamp unlockDateTime) {
        this.unlockDateTime = unlockDateTime;
    }

    public List<PCSearchSiteItemEntity> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<PCSearchSiteItemEntity> searchItems) {
        this.searchItems = searchItems;
    }
}
