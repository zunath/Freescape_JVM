package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestTypeDomain")
public class QuestTypeEntity {
    @Id
    @Column(name="QuestTypeID")
    private int questTypeID;

    @Column(name="Name")
    private String name;

    public int getQuestTypeID() {
        return questTypeID;
    }

    public void setQuestTypeID(int questTypeID) {
        this.questTypeID = questTypeID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
