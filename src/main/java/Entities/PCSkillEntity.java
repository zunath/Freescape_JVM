package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PCSkills")
public class PCSkillEntity {

    @Id
    @Column(name = "PCSkillID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcSkillID;

    @Column(name = "PlayerID")
    private String playerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SkillID")
    private SkillEntity skill;

    @Column(name = "XP")
    private int xp;

    @Column(name = "Rank")
    private int rank;

    public int getPcSkillID() {
        return pcSkillID;
    }

    public void setPcSkillID(int pcSkillID) {
        this.pcSkillID = pcSkillID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public SkillEntity getSkill() {
        return skill;
    }

    public void setSkill(SkillEntity skill) {
        this.skill = skill;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
