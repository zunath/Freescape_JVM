package Placeable.CraftingForge;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnHeartbeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject forge) {
        int charges = getLocalInt(forge, "FORGE_CHARGES");

        if(charges > 0)
        {
            charges--;
            setLocalInt(forge, "FORGE_CHARGES", charges);
        }

        if(charges <= 0)
        {
            NWObject flames = getLocalObject(forge, "FORGE_FLAMES");
            destroyObject(flames, 0.0f);
        }
    }
}
