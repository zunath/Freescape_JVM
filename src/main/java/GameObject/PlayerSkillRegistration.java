package GameObject;

import com.sun.tools.javac.util.Pair;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSkillRegistration {

    private NWObject pc;
    private HashMap<Integer, PlayerSkillPointTracker> skillPoints;

    PlayerSkillRegistration(NWObject oPC)
    {
        skillPoints = new HashMap<>();
        pc = oPC;
    }

    public void AddSkillPointRegistration(int skillID, int level)
    {
        PlayerSkillPointTracker tracker = skillPoints.getOrDefault(skillID, new PlayerSkillPointTracker(skillID));
        tracker.setPoints(tracker.getPoints() + 1);

        // Always take the lowest level.
        if(tracker.getRegisteredLevel() == -1 || level < tracker.getRegisteredLevel())
        {
            tracker.setRegisteredLevel(level);
        }

        skillPoints.put(skillID, tracker);
    }

    public ArrayList<Pair<Integer, PlayerSkillPointTracker>> GetSkillRegistrationPoints()
    {
        int size = skillPoints.size();
        Integer[] keys = skillPoints.keySet().toArray(new Integer[size]);
        PlayerSkillPointTracker[] values = skillPoints.values().toArray(new PlayerSkillPointTracker[size]);

        ArrayList<Pair<Integer, PlayerSkillPointTracker>> registrationPoints = new ArrayList<>();
        for(int x = 0; x < skillPoints.size(); x++)
        {
            Pair<Integer, PlayerSkillPointTracker> pair = new Pair<>(keys[x], values[x]);
            registrationPoints.add(pair);
        }

        return registrationPoints;
    }

    public int GetTotalSkillRegistrationPoints()
    {
        int totalPoints = 0;

        for(Pair<Integer, PlayerSkillPointTracker> reg : GetSkillRegistrationPoints())
        {
            totalPoints += reg.snd.getPoints();
        }

        return totalPoints;
    }

    public NWObject getPC() {
        return pc;
    }
}
