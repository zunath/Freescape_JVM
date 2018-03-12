package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PerkLevelSkillRequirements")
public class PerkLevelSkillRequirementEntity {

    @Id
    @Column(name = "PerkLevelSkillRequirementID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int perkLevelSkillRequirementID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PerkLevelID")
    private PerkLevelEntity perkLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SkillID")
    private SkillEntity skill;

    @Column(name = "RequiredRank")
    private int requiredRank;

    public int getPerkLevelSkillRequirementID() {
        return perkLevelSkillRequirementID;
    }

    public void setPerkLevelSkillRequirementID(int perkLevelSkillRequirementID) {
        this.perkLevelSkillRequirementID = perkLevelSkillRequirementID;
    }

    public PerkLevelEntity getPerkLevel() {
        return perkLevel;
    }

    public void setPerkLevel(PerkLevelEntity perkLevel) {
        this.perkLevel = perkLevel;
    }

    public SkillEntity getSkill() {
        return skill;
    }

    public void setSkill(SkillEntity skill) {
        this.skill = skill;
    }

    public int getRequiredRank() {
        return requiredRank;
    }

    public void setRequiredRank(int requiredRank) {
        this.requiredRank = requiredRank;
    }

}
