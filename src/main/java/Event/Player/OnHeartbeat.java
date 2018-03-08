package Event.Player;

import Common.IScriptEventHandler;
import GameSystems.CustomEffectSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("unused")
public class OnHeartbeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        CustomEffectSystem.OnPlayerHeartbeat(oPC);
    }
}
