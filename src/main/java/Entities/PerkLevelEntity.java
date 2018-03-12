package Entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PerkLevels")
public class PerkLevelEntity {

    @Id
    @Column(name = "PerkLevelID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int perkLevelID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PerkID")
    private PerkEntity perk;

    @Column(name = "Level")
    private int level;

    @Column(name = "Price")
    private int price;

    @Column(name = "Description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perkLevel", fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<PerkLevelSkillRequirementEntity> skillRequirements;

    public int getPerkLevelID() {
        return perkLevelID;
    }

    public void setPerkLevelID(int perkLevelID) {
        this.perkLevelID = perkLevelID;
    }

    public PerkEntity getPerk() {
        return perk;
    }

    public void setPerk(PerkEntity perk) {
        this.perk = perk;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PerkLevelSkillRequirementEntity> getSkillRequirements() {
        return skillRequirements;
    }

    public void setSkillRequirements(List<PerkLevelSkillRequirementEntity> skillRequirements) {
        this.skillRequirements = skillRequirements;
    }
}
