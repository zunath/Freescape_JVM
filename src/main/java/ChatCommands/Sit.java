package ChatCommands;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.AnimationLooping;

import static org.nwnx.nwnx2.jvm.NWScript.actionPlayAnimation;

public class Sit implements IChatCommand {
    @Override
    public boolean CanUse(NWObject user) {
        return true;
    }

    @Override
    public void DoAction(NWObject user) {
        Scheduler.assign(user, () -> actionPlayAnimation(AnimationLooping.SIT_CROSS, 1.0f, 9999.9f));
    }
}
