package Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LootTables")
public class LootTableEntity {

    @Id
    @Column(name ="LootTableID")
    private int lootTableID;

    @Column(name = "Name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lootTable", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<LootTableItemEntity> lootTableItems;

    public LootTableEntity()
    {
        lootTableItems = new ArrayList<>();
    }


    public int getLootTableID() {
        return lootTableID;
    }

    public void setLootTableID(int lootTableID) {
        this.lootTableID = lootTableID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LootTableItemEntity> getLootTableItems() {
        return lootTableItems;
    }

    public void setLootTableItems(List<LootTableItemEntity> lootTableItems) {
        this.lootTableItems = lootTableItems;
    }
}
