package Entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Perks")
public class PerkEntity {

    @Id
    @Column(name = "PerkID")
    private int perkID;

    @Column(name = "Name")
    private String name;

    @Column(name = "FeatID")
    private Integer featID;

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
    @JoinColumn(name = "PerkCategoryID")
    private PerkCategoryEntity category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CooldownCategoryID")
    private CooldownCategoryEntity cooldown;

    @Column(name = "IsQueuedWeaponSkill")
    private boolean isQueuedWeaponSkill;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perk", fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<PerkLevelEntity> levels;

    public int getPerkID() {
        return perkID;
    }

    public void setPerkID(int perkID) {
        this.perkID = perkID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFeatID() {
        return featID;
    }

    public void setFeatID(Integer featID) {
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

    public PerkCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(PerkCategoryEntity category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CooldownCategoryEntity getCooldown() {
        return cooldown;
    }

    public void setCooldown(CooldownCategoryEntity cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isQueuedWeaponSkill() {
        return isQueuedWeaponSkill;
    }

    public void setQueuedWeaponSkill(boolean queuedWeaponSkill) {
        isQueuedWeaponSkill = queuedWeaponSkill;
    }

    public List<PerkLevelEntity> getLevels() {
        return levels;
    }

    public void setLevels(List<PerkLevelEntity> levels) {
        this.levels = levels;
    }
}
