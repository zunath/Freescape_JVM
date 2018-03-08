package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestPrerequisites")
public class QuestPrerequisiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestPrerequisiteID")
    private int questPrerequisiteID;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QuestID", updatable = false, insertable = false)
    private QuestEntity quest;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RequiredQuestID", updatable = false, insertable = false)
    private QuestEntity requiredQuest;

    public int getQuestPrerequisiteID() {
        return questPrerequisiteID;
    }

    public QuestEntity getQuest() {
        return quest;
    }

    public void setQuest(QuestEntity quest) {
        this.quest = quest;
    }
    public void setQuestPrerequisiteID(int questPrerequisiteID) {
        this.questPrerequisiteID = questPrerequisiteID;
    }

    public QuestEntity getRequiredQuest() {
        return requiredQuest;
    }

    public void setRequiredQuest(QuestEntity requiredQuest) {
        this.requiredQuest = requiredQuest;
    }
}
