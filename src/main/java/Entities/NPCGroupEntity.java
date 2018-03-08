package Entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="NPCGroups")
public class NPCGroupEntity {
    @Id
    @Column(name = "NPCGroupID")
    private int npcGroupID;

    @Column(name = "Name")
    private String name;

    public int getNPCGroupID() {
        return npcGroupID;
    }

    public void setNPCGroupID(int npcGroupID) {
        this.npcGroupID = npcGroupID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
