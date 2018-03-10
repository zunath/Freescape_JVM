package GameObject;

public class PlayerSkillPointTracker {
    private int skillID;
    private int points;
    private int registeredLevel;

    PlayerSkillPointTracker(int skillID)
    {
        this.skillID = skillID;
        points = 0;
        registeredLevel = -1;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRegisteredLevel() {
        return registeredLevel;
    }

    public void setRegisteredLevel(int registeredLevel) {
        this.registeredLevel = registeredLevel;
    }
}
