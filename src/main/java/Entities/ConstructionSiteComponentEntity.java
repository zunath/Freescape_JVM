package Entities;

import javax.persistence.*;

@Entity
@Table(name = "ConstructionSiteComponents")
public class ConstructionSiteComponentEntity {

    @Id
    @Column(name = "ConstructionSiteComponentID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int constructionSiteComponentID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ConstructionSiteID")
    private ConstructionSiteEntity constructionSite;

    @Column(name = "Quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StructureComponentID")
    private StructureComponentEntity structureComponent;

    public int getConstructionSiteComponentID() {
        return constructionSiteComponentID;
    }

    public void setConstructionSiteComponentID(int constructionSiteComponentID) {
        this.constructionSiteComponentID = constructionSiteComponentID;
    }

    public ConstructionSiteEntity getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(ConstructionSiteEntity constructionSite) {
        this.constructionSite = constructionSite;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StructureComponentEntity getStructureComponent() {
        return structureComponent;
    }

    public void setStructureComponent(StructureComponentEntity structureComponent) {
        this.structureComponent = structureComponent;
    }
}
