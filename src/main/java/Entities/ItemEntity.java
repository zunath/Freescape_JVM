package Entities;

import javax.persistence.*;

@Entity
@Table(name = "Items")
public class ItemEntity {

    @Id
    @Column(name = "Resref")
    private String resref;

    @Column(name = "AC")
    private int ac;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ItemTypeID")
    private ItemTypeEntity itemType;

    @Column(name = "RecommendedLevel")
    private int recommendedLevel;

    @Column(name = "LoggingBonus")
    private int loggingBonus;

    @Column(name = "MiningBonus")
    private int miningBonus;

    @Column(name = "CastingSpeed")
    private int castingSpeed;

    @Column(name = "CraftBonusMetalworking")
    private int craftBonusMetalworking;

    @Column(name = "CraftBonusArmorsmith")
    private int craftBonusArmorsmith;

    @Column(name = "CraftBonusWeaponsmith")
    private int craftBonusWeaponsmith;

    @Column(name = "CraftBonusCooking")
    private int craftBonusCooking;

    @Column(name = "DurabilityPoints")
    private int durabilityPoints;

    @Column(name = "AssociatedSkillID")
    private int associatedSkillID;

    @Column(name = "CraftTierLevel")
    private int craftTierLevel;

    @Column(name = "CraftBonusWoodworking")
    private int craftBonusWoodworking;

    @Column(name = "Weight")
    private int weight;

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public ItemTypeEntity getItemType() {
        return itemType;
    }

    public void setItemType(ItemTypeEntity itemType) {
        this.itemType = itemType;
    }

    public int getRecommendedLevel() {
        return recommendedLevel;
    }

    public void setRecommendedLevel(int recommendedLevel) {
        this.recommendedLevel = recommendedLevel;
    }

    public int getLoggingBonus() {
        return loggingBonus;
    }

    public void setLoggingBonus(int loggingBonus) {
        this.loggingBonus = loggingBonus;
    }

    public int getMiningBonus() {
        return miningBonus;
    }

    public void setMiningBonus(int miningBonus) {
        this.miningBonus = miningBonus;
    }

    public int getCastingSpeed() {
        return castingSpeed;
    }

    public void setCastingSpeed(int castingSpeed) {
        this.castingSpeed = castingSpeed;
    }

    public int getCraftBonusMetalworking() {
        return craftBonusMetalworking;
    }

    public void setCraftBonusMetalworking(int craftBonusMetalworking) {
        this.craftBonusMetalworking = craftBonusMetalworking;
    }

    public int getCraftBonusArmorsmith() {
        return craftBonusArmorsmith;
    }

    public void setCraftBonusArmorsmith(int craftBonusArmorsmith) {
        this.craftBonusArmorsmith = craftBonusArmorsmith;
    }

    public int getCraftBonusWeaponsmith() {
        return craftBonusWeaponsmith;
    }

    public void setCraftBonusWeaponsmith(int craftBonusWeaponsmith) {
        this.craftBonusWeaponsmith = craftBonusWeaponsmith;
    }

    public int getCraftBonusCooking() {
        return craftBonusCooking;
    }

    public void setCraftBonusCooking(int craftBonusCooking) {
        this.craftBonusCooking = craftBonusCooking;
    }

    public int getDurabilityPoints() {
        return durabilityPoints;
    }

    public void setDurabilityPoints(int durabilityPoints) {
        this.durabilityPoints = durabilityPoints;
    }

    public int getAssociatedSkillID() {
        return associatedSkillID;
    }

    public void setAssociatedSkillID(int associatedSkillID) {
        this.associatedSkillID = associatedSkillID;
    }

    public int getCraftTierLevel() {
        return craftTierLevel;
    }

    public void setCraftTierLevel(int craftTierLevel) {
        this.craftTierLevel = craftTierLevel;
    }

    public int getCraftBonusWoodworking() {
        return craftBonusWoodworking;
    }

    public void setCraftBonusWoodworking(int craftBonusWoodworking) {
        this.craftBonusWoodworking = craftBonusWoodworking;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
