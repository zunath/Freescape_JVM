package Entities;

import javax.persistence.*;

@Entity
@Table(name = "StructureComponents")
public class StructureComponentEntity {

    @Id
    @Column(name = "StructureComponentID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int structureComponentID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StructureBlueprintID")
    private StructureBlueprintEntity blueprint;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "Quantity")
    private int quantity;

    public int getStructureComponentID() {
        return structureComponentID;
    }

    public void setStructureComponentID(int structureComponentID) {
        this.structureComponentID = structureComponentID;
    }

    public StructureBlueprintEntity getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(StructureBlueprintEntity blueprint) {
        this.blueprint = blueprint;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
