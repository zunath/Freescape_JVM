package Common;

import Helper.ErrorHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.SchedulerListener;


public class EventReceiver implements SchedulerListener {
    public void postFlushQueues(int remainingTokens) {}
    public void missedToken(NWObject objSelf, String token) {}
    public void context(NWObject objSelf) {}

    public void event(NWObject objSelf, String event) {

        // Try to locate a matching class name based on the event passed in from NWN JVM_EVENT call.
        try {
            Class scriptClass = Class.forName(event);
            IScriptEventHandler script = (IScriptEventHandler)scriptClass.newInstance();
            script.runScript(objSelf);
            Scheduler.flushQueues();
        }
        catch(Exception ex) {
            ErrorHelper.HandleException(ex, "Common.EventReceiver was unable to execute class method: " + event + ".runScript()");
        }
    }
}
