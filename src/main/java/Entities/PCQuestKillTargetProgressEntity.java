package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PCQuestKillTargetProgress")
public class PCQuestKillTargetProgressEntity {

    @Id
    @Column(name = "PCQuestKillTargetProgressID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcQuestKillTargetProgressID;

    @Column(name = "PlayerID")
    private String playerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PCQuestStatusID")
    private PCQuestStatusEntity pcQuestStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NPCGroupID")
    private NPCGroupEntity npcGroup;

    @Column(name = "RemainingToKill")
    private int remainingToKill;

    public int getPcQuestKillTargetProgressID() {
        return pcQuestKillTargetProgressID;
    }

    public void setPcQuestKillTargetProgressID(int pcQuestKillTargetProgressID) {
        this.pcQuestKillTargetProgressID = pcQuestKillTargetProgressID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public PCQuestStatusEntity getPcQuestStatus() {
        return pcQuestStatus;
    }

    public void setPcQuestStatus(PCQuestStatusEntity pcQuestStatus) {
        this.pcQuestStatus = pcQuestStatus;
    }

    public NPCGroupEntity getNpcGroup() {
        return npcGroup;
    }

    public void setNpcGroup(NPCGroupEntity npcGroupID) {
        this.npcGroup = npcGroupID;
    }

    public int getRemainingToKill() {
        return remainingToKill;
    }

    public void setRemainingToKill(int remainingToKill) {
        this.remainingToKill = remainingToKill;
    }
}
