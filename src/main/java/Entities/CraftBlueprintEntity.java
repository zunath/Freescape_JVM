package Entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CraftBlueprints")
public class CraftBlueprintEntity {

    @Id
    @Column(name = "CraftBlueprintID")
    private int craftBlueprintID;

    @Column(name = "ItemName")
    private String itemName;

    @Column(name = "ItemResref")
    private String itemResref;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "Level")
    private int level;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blueprint", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CraftComponentEntity> components;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CraftCategoryID")
    private CraftBlueprintCategoryEntity category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SkillID")
    private SkillEntity skill;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CraftDeviceID")
    private CraftDeviceEntity device;

    @Column(name = "RequiredPerkLevel")
    private int requiredPerkLevel;

    @Column(name = "IsActive")
    private boolean isActive;

    public int getCraftBlueprintID() {
        return craftBlueprintID;
    }

    public void setCraftBlueprintID(int craftBlueprintID) {
        this.craftBlueprintID = craftBlueprintID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<CraftComponentEntity> getComponents() {
        return components;
    }

    public void setComponents(List<CraftComponentEntity> components) {
        this.components = components;
    }

    public CraftBlueprintCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CraftBlueprintCategoryEntity category) {
        this.category = category;
    }

    public SkillEntity getSkill() {
        return skill;
    }

    public void setSkill(SkillEntity skill) {
        this.skill = skill;
    }

    public CraftDeviceEntity getDevice() {
        return device;
    }

    public void setDevice(CraftDeviceEntity device) {
        this.device = device;
    }

    public int getRequiredPerkLevel() {
        return requiredPerkLevel;
    }

    public void setRequiredPerkLevel(int requiredPerkLevel) {
        this.requiredPerkLevel = requiredPerkLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
