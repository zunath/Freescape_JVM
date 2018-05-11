package ChatCommands;

import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.setTime;

public class Day implements IChatCommand {
    @Override
    public boolean CanUse(NWObject user) {
        return true;
    }

    @Override
    public void DoAction(NWObject user) {
        setTime(8, 0, 0, 0);
    }
}
