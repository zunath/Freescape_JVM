package ChatCommands;

import GameSystems.DeathSystem;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.sendMessageToPC;

public class Stuck implements IChatCommand {
    @Override
    public boolean CanUse(NWObject user) {
        return true;
    }

    @Override
    public void DoAction(NWObject user) {
        DeathSystem.TeleportPlayerToBindPoint(user);
        sendMessageToPC(user, "Alpha feature: Returning to bind point. Please report bugs on Discord/GitHub. And for the love of all that is Zunath, don't abuse this!");
    }
}
