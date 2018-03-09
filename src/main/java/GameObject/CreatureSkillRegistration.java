package GameObject;

import org.nwnx.nwnx2.jvm.NWObject;

import java.util.HashMap;

public class CreatureSkillRegistration {
    private String creatureID;
    private HashMap<String, PlayerSkillRegistration> registrations;

    public CreatureSkillRegistration(String creatureID)
    {
        registrations = new HashMap<>();
        this.creatureID = creatureID;
    }

    private PlayerSkillRegistration GetRegistration(NWObject oPC)
    {
        PlayerGO playerGO = new PlayerGO(oPC);
        PlayerSkillRegistration reg = registrations.getOrDefault(playerGO.getUUID(), null);
        if(reg == null)
        {
            reg = new PlayerSkillRegistration(oPC);
            registrations.put(playerGO.getUUID(), reg);
        }

        return reg;
    }

    public void AddSkillRegistrationPoint(NWObject oPC, int skillID)
    {
        PlayerSkillRegistration reg = GetRegistration(oPC);
        reg.AddSkillPointRegistration(skillID);
    }

    public PlayerSkillRegistration[] GetAllRegistrations()
    {
        return registrations.values().toArray(new PlayerSkillRegistration[registrations.size()]);
    }

    public String GetCreatureID()
    {
        return creatureID;
    }

    public boolean IsRegistrationEmpty()
    {
        return registrations.isEmpty();
    }

    public void RemovePlayerRegistration(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);

        if(registrations.getOrDefault(pcGO.getUUID(), null) != null)
        {
            registrations.remove(pcGO.getUUID());
        }
    }
}
