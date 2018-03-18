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
}
