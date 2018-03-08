package Entities;

import javax.persistence.*;

@Entity
@Table(name = "CraftBlueprintComponents")
public class CraftComponentEntity {

    @Id
    @Column(name = "CraftComponentID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int craftComponentID;

    @Column(name = "ItemResref")
    private String itemResref;

    @Column(name = "Quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CraftBlueprintID")
    private CraftBlueprintEntity blueprint;

    public int getCraftComponentID() {
        return craftComponentID;
    }

    public void setCraftComponentID(int craftComponentID) {
        this.craftComponentID = craftComponentID;
    }

    public String getItemResref() {
        return itemResref;
    }

    public void setItemResref(String itemResref) {
        this.itemResref = itemResref;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CraftBlueprintEntity getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(CraftBlueprintEntity blueprint) {
        this.blueprint = blueprint;
    }
}
