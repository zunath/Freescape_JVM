package GameObject;

import com.sun.tools.javac.util.Pair;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSkillRegistration {

    private NWObject pc;
    private HashMap<Integer, Integer> skillPoints;

    public PlayerSkillRegistration(NWObject oPC)
    {
        skillPoints = new HashMap<>();
        pc = oPC;
    }

    public void AddSkillPointRegistration(int skillID)
    {
        int points = skillPoints.getOrDefault(skillID, 0) + 1;
        skillPoints.put(skillID, points);
    }

    public ArrayList<Pair<Integer, Integer>> GetSkillRegistrationPoints()
    {
        int size = skillPoints.size();
        Integer[] keys = skillPoints.keySet().toArray(new Integer[size]);
        Integer[] values = skillPoints.values().toArray(new Integer[size]);

        ArrayList<Pair<Integer, Integer>> registrationPoints = new ArrayList<>();
        for(int x = 0; x < skillPoints.size(); x++)
        {
            Pair<Integer, Integer> pair = new Pair<>(keys[x], values[x]);
            registrationPoints.add(pair);
        }

        return registrationPoints;
    }

    public int GetTotalSkillRegistrationPoints()
    {
        int totalPoints = 0;

        for(Pair<Integer, Integer> reg : GetSkillRegistrationPoints())
        {
            totalPoints += reg.snd;
        }

        return totalPoints;
    }

    public NWObject getPC() {
        return pc;
    }
}
