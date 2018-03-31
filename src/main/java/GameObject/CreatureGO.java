package GameObject;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class CreatureGO {
    private NWObject npc;

    public CreatureGO(NWObject npc)
    {
        this.npc = npc;
    }

    public NWObject getNpc() {
        return npc;
    }

    public void setNpc(NWObject npc) {
        this.npc = npc;
    }

    public String getGlobalUUID() {
        return NWScript.getLocalString(npc, "CREATURE_GLOBAL_UUID");
    }

    public void setGlobalUUID(String uuid) {
        NWScript.setLocalString(npc, "CREATURE_GLOBAL_UUID", uuid);
    }

    public int getCreatureID() {
        return NWScript.getLocalInt(npc, "CREATURE_ID");
    }

    public void setCreatureID(int value) {
        if(value < 1) value = 1;
        NWScript.setLocalInt(npc, "CREATURE_ID", value);
    }
}
