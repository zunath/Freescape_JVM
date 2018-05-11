package ChatCommands;

import org.nwnx.nwnx2.jvm.NWObject;

public interface IChatCommand {

    boolean CanUse(NWObject user);

    void DoAction(NWObject user);

}
