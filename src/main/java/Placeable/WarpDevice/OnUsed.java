package Placeable.WarpDevice;


import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.DurationType;

public class OnUsed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        final NWObject oPC = NWScript.getLastUsedBy();
        final String destination = NWScript.getLocalString(objSelf, "DESTINATION");
        int visualEffectID = NWScript.getLocalInt(objSelf, "VISUAL_EFFECT");

        if(visualEffectID > 0)
        {
            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(visualEffectID, false), oPC, 0.0f);
        }

        Scheduler.assign(oPC, () -> {
            NWLocation location = NWScript.getLocation(NWScript.getWaypointByTag(destination));
            NWScript.actionJumpToLocation(location);
        });

    }
}
