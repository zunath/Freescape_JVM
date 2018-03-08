package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestKillTargetList")
public class QuestKillTargetListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestKillTargetListID")
    private int questKillTargetListID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QuestID", updatable = false, insertable = false)
    private QuestEntity quest;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NPCGroupID", updatable = false, insertable = false)
    private NPCGroupEntity npcGroup;

    @Column(name = "Quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QuestStateID", updatable = false, insertable = false)
    private QuestStateEntity questState;


    public int getQuestKillTargetListID() {
        return questKillTargetListID;
    }

    public void setQuestKillTargetListID(int questKillTargetListID) {
        this.questKillTargetListID = questKillTargetListID;
    }

    public QuestEntity getQuest() {
        return quest;
    }

    public void setQuest(QuestEntity questID) {
        this.quest = questID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public NPCGroupEntity getNpcGroup() {
        return npcGroup;
    }

    public void setNpcGroup(NPCGroupEntity npcGroup) {
        this.npcGroup = npcGroup;
    }


    public QuestStateEntity getQuestState() {
        return questState;
    }

    public void setQuestState(QuestStateEntity questState) {
        this.questState = questState;
    }

}
