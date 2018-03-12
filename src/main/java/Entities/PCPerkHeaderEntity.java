package Entities;

import javax.persistence.*;

@Entity
@SqlResultSetMapping(name="PCPerkHeaderEntityResult", classes = {
        @ConstructorResult(targetClass = PCPerkHeaderEntity.class,
                columns = {@ColumnResult(name="PCPerkID"),
                           @ColumnResult(name="Name"),
                           @ColumnResult(name="Level"),
                           @ColumnResult(name="BonusDescription")})
})
public class PCPerkHeaderEntity {

    @Id
    private int pcPerkID;

    private String name;

    private int level;

    private String bonusDescription;


    public PCPerkHeaderEntity(int pcPerkID, String name, int level, String bonusDescription)
    {
        this.pcPerkID = pcPerkID;
        this.name = name;
        this.level = level;
        this.bonusDescription = bonusDescription;
    }


    public int getPcPerkID() {
        return pcPerkID;
    }

    public void setPcPerkID(int pcPerkID) {
        this.pcPerkID = pcPerkID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBonusDescription() {
        return bonusDescription;
    }

    public void setBonusDescription(String bonusDescription) {
        this.bonusDescription = bonusDescription;
    }
}
