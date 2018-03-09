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

    public String getUUID() {
        return NWScript.getLocalString(npc, "CREATURE_ID");
    }

    public void setUUID(String uuid) {
        NWScript.setLocalString(npc, "CREATURE_ID", uuid);
    }
}
