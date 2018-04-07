package Placeable.Resource;

import Common.IScriptEventHandler;
import Enumerations.PerkID;
import GameSystems.PerkSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnDeath implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = getLastKiller();
        NWLocation location = getLocation(objSelf);
        String resourceItemResref = getLocalString(objSelf, "RESOURCE_RESREF");
        String resourceSeedResref = getLocalString(objSelf, "RESOURCE_SEED_RESREF");

        createObject(ObjectType.ITEM, resourceItemResref, location, false, "");

        if(!resourceSeedResref.equals(""))
        {
            createObject(ObjectType.ITEM, resourceSeedResref, location, false, "");

            int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.SeedSearcher);
            if(perkLevel <= 0) return;

            if(ThreadLocalRandom.current().nextInt(100) + 1 <= perkLevel * 10)
            {
                createObject(ObjectType.ITEM, resourceSeedResref, location, false, "");
            }
        }
    }
}
