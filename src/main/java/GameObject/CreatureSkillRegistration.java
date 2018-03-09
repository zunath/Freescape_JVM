package GameObject;

import java.util.HashMap;

public class CreatureSkillRegistration {
    private HashMap<String, PlayerSkillRegistration> registrations;

    public CreatureSkillRegistration()
    {
        registrations = new HashMap<>();
    }

    private PlayerSkillRegistration GetRegistration(String playerUUID)
    {
        PlayerSkillRegistration reg = registrations.getOrDefault(playerUUID, null);
        if(reg == null)
        {
            reg = new PlayerSkillRegistration(playerUUID);
            registrations.put(playerUUID, reg);
        }

        return reg;
    }

    public void AddSkillRegistrationPoint(String playerUUID, int skillID)
    {
        PlayerSkillRegistration reg = GetRegistration(playerUUID);
        reg.AddSkillPointRegistration(skillID);
    }

    public PlayerSkillRegistration[] GetAllRegistrations()
    {
        return registrations.values().toArray(new PlayerSkillRegistration[registrations.size()]);
    }

}
