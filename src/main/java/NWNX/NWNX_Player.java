package NWNX;

import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

import static NWNX.NWNX_Core.*;

public class NWNX_Player {

    private static final String NWNX_Player = "NWNX_Player";

    public static void ForcePlaceableExamineWindow(NWObject player, NWObject placeable)
    {
        String sFunc = "ForcePlaceableExamineWindow";
        NWNX_PushArgumentObject(NWNX_Player, sFunc, placeable);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);
    }

    public static void StartGuiTimingBar(NWObject player, float seconds, String script)
    {
        // only one timing bar at a time!
        if (NWScript.getLocalInt(player, "GUI_TIMING_ACTIVE") > 0)
            return;

        String sFunc = "StartGuiTimingBar";
        NWNX_PushArgumentFloat(NWNX_Player, sFunc, seconds);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);

        int id = NWScript.getLocalInt(player, "GUI_TIMING_ID") + 1;
        NWScript.setLocalInt(player, "GUI_TIMING_ACTIVE", id);
        NWScript.setLocalInt(player, "GUI_TIMING_ID", id);

        Scheduler.delay(player, (long)seconds*1000, () -> StopGuiTimingBar(player, script, -1));
    }

    public static void StopGuiTimingBar(NWObject player, String script, int id)
    {
        int activeId = NWScript.getLocalInt(player, "GUI_TIMING_ACTIVE");
        // Either the timing event was never started, or it already finished.
        if (activeId == 0)
            return;

        // If id != -1, we ended up here through DelayCommand. Make sure it's for the right ID
        if (id != -1 && id != activeId)
            return;

        NWScript.deleteLocalInt(player, "GUI_TIMING_ACTIVE");

        String sFunc = "StopGuiTimingBar";
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);
        NWNX_CallFunction(NWNX_Player, sFunc);

        if(!script.equals(""))
        {
            ScriptHelper.RunJavaScript(player, script);
        }
    }


}
