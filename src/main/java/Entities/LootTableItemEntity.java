package Entities;

import javax.persistence.*;

@Entity
@Table(name = "LootTableItems")
public class LootTableItemEntity {

    @Id
    @Column(name ="LootTableItemID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lootTableItemID;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "Weight")
    private int weight;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "MaxQuantity")
    private int maxQuantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LootTableID")
    private LootTableEntity lootTable;

    public int getLootTableItemID() {
        return lootTableItemID;
    }

    public void setLootTableItemID(int lootTableItemID) {
        this.lootTableItemID = lootTableItemID;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LootTableEntity getLootTable() {
        return lootTable;
    }

    public void setLootTable(LootTableEntity lootTable) {
        this.lootTable = lootTable;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
}
