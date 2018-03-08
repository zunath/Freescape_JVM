package Entities;

import javax.persistence.*;

@Entity
@Table(name = "Abilities")
public class AbilityEntity {

    @Id
    @Column(name = "AbilityID")
    private int abilityID;

    @Column(name = "Name")
    private String name;

    @Column(name = "FeatID")
    private int featID;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "JavaScriptName")
    private String javaScriptName;

    @Column(name = "BaseManaCost")
    private int baseManaCost;

    @Column(name = "BaseCastingTime")
    private float baseCastingTime;

    @Column(name = "Description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AbilityCategoryID")
    private AbilityCategoryEntity category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AbilityCooldownCategoryID")
    private AbilityCooldownCategoryEntity cooldown;

    @Column(name = "IsQueuedWeaponSkill")
    private boolean isQueuedWeaponSkill;

    public int getAbilityID() {
        return abilityID;
    }

    public void setAbilityID(int abilityID) {
        this.abilityID = abilityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFeatID() {
        return featID;
    }

    public void setFeatID(int featID) {
        this.featID = featID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getJavaScriptName() {
        return javaScriptName;
    }

    public void setJavaScriptName(String javaScriptName) {
        this.javaScriptName = javaScriptName;
    }

    public int getBaseManaCost() {
        return baseManaCost;
    }

    public void setBaseManaCost(int baseManaCost) {
        this.baseManaCost = baseManaCost;
    }

    public float getBaseCastingTime() {
        return baseCastingTime;
    }

    public void setBaseCastingTime(float baseCastingTime) {
        this.baseCastingTime = baseCastingTime;
    }

    public AbilityCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(AbilityCategoryEntity category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AbilityCooldownCategoryEntity getCooldown() {
        return cooldown;
    }

    public void setCooldown(AbilityCooldownCategoryEntity cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isQueuedWeaponSkill() {
        return isQueuedWeaponSkill;
    }

    public void setQueuedWeaponSkill(boolean queuedWeaponSkill) {
        isQueuedWeaponSkill = queuedWeaponSkill;
    }
}
