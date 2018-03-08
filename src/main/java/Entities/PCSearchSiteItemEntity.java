package Entities;

import javax.persistence.*;

@Entity
@Table(name="PCSearchSiteItems")
public class PCSearchSiteItemEntity {

    @Id
    @Column(name = "PCSearchSiteItemID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int searchItemID;

    @Lob
    @Column(name = "SearchItem")
    private byte[] searchItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="SearchSiteID", referencedColumnName = "SearchSiteID"),
            @JoinColumn(name="PlayerID", referencedColumnName = "PlayerID")
    })
    private PCSearchSiteEntity searchSite;

    public int getSearchItemID() {
        return searchItemID;
    }

    public void setSearchItemID(int searchItemID) {
        this.searchItemID = searchItemID;
    }

    public byte[] getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(byte[] searchItem) {
        this.searchItem = searchItem;
    }

    public PCSearchSiteEntity getSearchSite() {
        return searchSite;
    }

    public void setSearchSite(PCSearchSiteEntity searchSite) {
        this.searchSite = searchSite;
    }
}
