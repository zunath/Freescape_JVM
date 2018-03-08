package Entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PCQuestStatus")
public class PCQuestStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PCQuestStatusID")
    private int pcQuestStatusID;

    @Column(name = "PlayerID")
    private String playerID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QuestID")
    private QuestEntity quest;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CurrentQuestStateID")
    private QuestStateEntity currentQuestState;

    @Column(name = "CompletionDate")
    private Date completionDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SelectedItemRewardID")
    private QuestRewardItemEntity selectedItemReward;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pcQuestStatus", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PCQuestKillTargetProgressEntity> pcKillTargets;

    public int getPcQuestStatusID() {
        return pcQuestStatusID;
    }

    public void setPcQuestStatusID(int pcQuestStatusID) {
        this.pcQuestStatusID = pcQuestStatusID;
    }

    public QuestEntity getQuest() {
        return quest;
    }

    public void setQuest(QuestEntity quest) {
        this.quest = quest;
    }

    public QuestStateEntity getCurrentQuestState() {
        return currentQuestState;
    }

    public void setCurrentQuestState(QuestStateEntity currentQuestState) {
        this.currentQuestState = currentQuestState;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public QuestRewardItemEntity getSelectedItemReward() {
        return selectedItemReward;
    }

    public void setSelectedItemReward(QuestRewardItemEntity selectedItemReward) {
        this.selectedItemReward = selectedItemReward;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public List<PCQuestKillTargetProgressEntity> getPcKillTargets() {
        return pcKillTargets;
    }

    public void setPcKillTargets(List<PCQuestKillTargetProgressEntity> pcKillTargets) {
        this.pcKillTargets = pcKillTargets;
    }
}
