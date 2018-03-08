package Entities;

import javax.persistence.*;

@Entity
@Table(name = "SkillXPRequirement")
public class SkillXPRequirementEntity {

    @Id
    @Column(name = "SkillXPRequirementID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int skillXPRequirementID;

    @Column(name = "SkillID")
    private int skillID;

    @Column(name = "Rank")
    private int rank;

    @Column(name = "XP")
    private int xp;

    public int getSkillXPRequirementID() {
        return skillXPRequirementID;
    }

    public void setSkillXPRequirementID(int skillXPRequirementID) {
        this.skillXPRequirementID = skillXPRequirementID;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
