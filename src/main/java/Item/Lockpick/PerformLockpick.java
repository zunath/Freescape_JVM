package Item.Lockpick;

import Helper.ColorToken;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

@SuppressWarnings("UnusedDeclaration")
public class PerformLockpick implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oPC) {

        final NWObject oTarget = NWScript.getLocalObject(oPC, "LOCKPICK_TEMP_UNLOCKING_OBJECT");

        NWScript.floatingTextStringOnCreature(ColorToken.Purple() + "Lockpicking complete! The object is now unlocked." + ColorToken.End(), oPC, false);
        NWScript.setLocked(oTarget, false);

        Scheduler.delay(oTarget, 12000, () -> LockAndClose(oTarget));

        NWScript.deleteLocalObject(oPC, "LOCKPICK_TEMP_UNLOCKING_OBJECT");
    }

    private void LockAndClose(final NWObject oTarget)
    {
        NWScript.setLocked(oTarget, true);
        Scheduler.assign(oTarget, () -> NWScript.actionCloseDoor(oTarget));
    }

}
