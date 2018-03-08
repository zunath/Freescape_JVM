package Creature.Zombie;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.DoorAction;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

@SuppressWarnings("unused")
public class Zombie_OnBlocked implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oZombie) {
        NWObject oTarget = NWScript.getAttackTarget(oZombie);
        NWObject oDoor = NWScript.getBlockingDoor();
        int iType = NWScript.getObjectType(oDoor);
        boolean isLocked = NWScript.getLocked(oDoor);

        if(isLocked)
        {
            Scheduler.assign(oZombie, () -> NWScript.clearAllActions(false));
        }
        else if(iType == ObjectType.DOOR && !NWScript.getIsOpen(oDoor))
        {
            NWScript.setLocalObject(oZombie, "PreDoorBashTarget", oTarget);
            NWScript.doDoorAction(oDoor, DoorAction.BASH);
        }
    }
}
