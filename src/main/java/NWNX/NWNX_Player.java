package NWNX;

import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

import static NWNX.NWNX_Core.*;
import static org.nwnx.nwnx2.jvm.NWScript.*;

public class NWNX_Player {
    private static final String NWNX_Player = "NWNX_Player";

    // Force display placeable examine window for player
    public static void ForcePlaceableExamineWindow(NWObject player, NWObject placeable)
    {
        String sFunc = "ForcePlaceableExamineWindow";
        NWNX_PushArgumentObject(NWNX_Player, sFunc, placeable);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);
    }

    public static void StopGuiTimingBar(NWObject player, String script, int id)
    {
        int activeId = getLocalInt(player, "NWNX_PLAYER_GUI_TIMING_ACTIVE");
        // Either the timing event was never started, or it already finished.
        if (activeId == 0)
            return;

        // If id != -1, we ended up here through DelayCommand. Make sure it's for the right ID
        if (id != -1 && id != activeId)
            return;

        deleteLocalInt(player, "NWNX_PLAYER_GUI_TIMING_ACTIVE");

        String sFunc = "StopGuiTimingBar";
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);
        NWNX_CallFunction(NWNX_Player, sFunc);

        if(!script.equals(""))
        {
            // "." is an invalid character in NWN script files, but valid for the Java classes.
            // Assume this is intended to be a Java call.
            if(script.contains("."))
            {
                ScriptHelper.RunJavaScript(player, script);
            }
            // Everything else is assumed to be an NWN script.
            else
            {
                NWScript.executeScript(script, player);
            }
        }
    }

    // Starts displaying a timing bar.
    // Will run a script at the end of the timing bar, if specified.
    public static void StartGuiTimingBar(NWObject player, float seconds, String script)
    {
        // only one timing bar at a time!
        if (getLocalInt(player, "NWNX_PLAYER_GUI_TIMING_ACTIVE") == 1)
            return;

        String sFunc = "StartGuiTimingBar";
        NWNX_PushArgumentFloat(NWNX_Player, sFunc, seconds);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);

        int id = getLocalInt(player, "NWNX_PLAYER_GUI_TIMING_ID") + 1;
        setLocalInt(player, "NWNX_PLAYER_GUI_TIMING_ACTIVE", id);
        setLocalInt(player, "NWNX_PLAYER_GUI_TIMING_ID", id);

        Scheduler.delay(player, (long)seconds*1000, () -> StopGuiTimingBar(player, script, -1));

    }

    // Stops displaying a timing bar.
// Runs a script if specified.
    public static void StopGuiTimingBar(NWObject player, String script)
    {
        StopGuiTimingBar(player, script, -1);
    }

    // Sets whether the player should always walk when given movement commands.
// If true, clicking on the ground or using WASD will trigger walking instead of running.
    public static void SetAlwaysWalk(NWObject player, int bWalk)
    {
        String sFunc = "SetAlwaysWalk";
        NWNX_PushArgumentInt(NWNX_Player, sFunc, bWalk);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);
    }

    // Gets the player's quickbar slot info
    QuickBarSlot NWNX_Player_GetQuickBarSlot(NWObject player, int slot)
    {
        String sFunc = "GetQuickBarSlot";
        QuickBarSlot qbs = new QuickBarSlot();

        NWNX_PushArgumentInt(NWNX_Player, sFunc, slot);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);
        NWNX_CallFunction(NWNX_Player, sFunc);

        qbs.oAssociate     = NWNX_GetReturnValueObject(NWNX_Player, sFunc);
        qbs.nAssociateType = NWNX_GetReturnValueInt(NWNX_Player,    sFunc);
        qbs.nDomainLevel   = NWNX_GetReturnValueInt(NWNX_Player,    sFunc);
        qbs.nMetaType      = NWNX_GetReturnValueInt(NWNX_Player,    sFunc);
        qbs.nINTParam1     = NWNX_GetReturnValueInt(NWNX_Player,    sFunc);
        qbs.sToolTip       = NWNX_GetReturnValueString(NWNX_Player, sFunc);
        qbs.sCommandLine   = NWNX_GetReturnValueString(NWNX_Player, sFunc);
        qbs.sCommandLabel  = NWNX_GetReturnValueString(NWNX_Player, sFunc);
        qbs.sResRef        = NWNX_GetReturnValueString(NWNX_Player, sFunc);
        qbs.nMultiClass    = NWNX_GetReturnValueInt(NWNX_Player,    sFunc);
        qbs.nObjectType    = NWNX_GetReturnValueInt(NWNX_Player,    sFunc);
        qbs.oSecondaryItem = NWNX_GetReturnValueObject(NWNX_Player, sFunc);
        qbs.oItem          = NWNX_GetReturnValueObject(NWNX_Player, sFunc);

        return qbs;
    }

    // Sets a player's quickbar slot
    public static void SetQuickBarSlot(NWObject player, int slot, QuickBarSlot qbs)
    {
        String sFunc = "SetQuickBarSlot";

        NWNX_PushArgumentObject(NWNX_Player, sFunc, qbs.oItem);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, qbs.oSecondaryItem);
        NWNX_PushArgumentInt(NWNX_Player,    sFunc, qbs.nObjectType);
        NWNX_PushArgumentInt(NWNX_Player,    sFunc, qbs.nMultiClass);
        NWNX_PushArgumentString(NWNX_Player, sFunc, qbs.sResRef);
        NWNX_PushArgumentString(NWNX_Player, sFunc, qbs.sCommandLabel);
        NWNX_PushArgumentString(NWNX_Player, sFunc, qbs.sCommandLine);
        NWNX_PushArgumentString(NWNX_Player, sFunc, qbs.sToolTip);
        NWNX_PushArgumentInt(NWNX_Player,    sFunc, qbs.nINTParam1);
        NWNX_PushArgumentInt(NWNX_Player,    sFunc, qbs.nMetaType);
        NWNX_PushArgumentInt(NWNX_Player,    sFunc, qbs.nDomainLevel);
        NWNX_PushArgumentInt(NWNX_Player,    sFunc, qbs.nAssociateType);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, qbs.oAssociate);

        NWNX_PushArgumentInt(NWNX_Player, sFunc, slot);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);
        NWNX_CallFunction(NWNX_Player, sFunc);
    }

}
