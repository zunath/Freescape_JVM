package Entities;

import javax.persistence.*;

@Entity
@Table(name = "ResearchBlueprints")
public class ResearchBlueprintEntity {

    @Id
    @Column(name = "ResearchBlueprintID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int researchBlueprintID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CraftBlueprintID")
    private CraftBlueprintEntity craftBlueprint;

    @Column(name = "IsActive")
    private boolean isActive;

    @Column(name = "Price")
    private int price;

    @Column(name = "ResearchPoints")
    private long researchPoints;

    @Column(name = "SkillRequired")
    private int skillRequired;

    public int getResearchBlueprintID() {
        return researchBlueprintID;
    }

    public void setResearchBlueprintID(int researchBlueprintID) {
        this.researchBlueprintID = researchBlueprintID;
    }

    public CraftBlueprintEntity getCraftBlueprint() {
        return craftBlueprint;
    }

    public void setCraftBlueprint(CraftBlueprintEntity craftBlueprint) {
        this.craftBlueprint = craftBlueprint;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getResearchPoints() {
        return researchPoints;
    }

    public void setResearchPoints(long researchPoints) {
        this.researchPoints = researchPoints;
    }

    public int getSkillRequired() {
        return skillRequired;
    }

    public void setSkillRequired(int skillRequired) {
        this.skillRequired = skillRequired;
    }
}
