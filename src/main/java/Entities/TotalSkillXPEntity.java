package Entities;

import javax.persistence.*;

@Entity
@SqlResultSetMapping(name="TotalSkillXPResult", classes = {
        @ConstructorResult(targetClass = TotalSkillXPEntity.class,
                columns = {@ColumnResult(name="SkillID"), @ColumnResult(name="TotalSkillXP")})
})
public class TotalSkillXPEntity {

    @Id
    private int skillID;
    private int totalSkillXP;

    public TotalSkillXPEntity(int skillID, int totalSkillXP)
    {
        this.skillID = skillID;
        this.totalSkillXP = totalSkillXP;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public int getTotalSkillXP() {
        return totalSkillXP;
    }

    public void setTotalSkillXP(int totalSkillXP) {
        this.totalSkillXP = totalSkillXP;
    }
}
