package NWNX;

import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import static NWNX.NWNX_Core.*;

public class NWNX_Events {

    // Scripts can subscribe to events.
    // Some events are dispatched via the NWNX plugin (see NWNX_EVENTS_EVENT_* constants).
    // Others can be signalled via script code (see NWNX_Events_SignalEvent).
    public static void SubscribeEvent(String evt, String script)
    {
        NWNX_PushArgumentString("NWNX_Events", "SUBSCRIBE_EVENT", script);
        NWNX_PushArgumentString("NWNX_Events", "SUBSCRIBE_EVENT", evt);
        NWNX_CallFunction("NWNX_Events", "SUBSCRIBE_EVENT");
    }

    // Pushes event data at the provided tag, which subscribers can access with GetEventData.
    // This should be called BEFORE SignalEvent.
    public static void PushEventData(String tag, String data)
    {
        NWNX_PushArgumentString("NWNX_Events", "PUSH_EVENT_DATA", data);
        NWNX_PushArgumentString("NWNX_Events", "PUSH_EVENT_DATA", tag);
        NWNX_CallFunction("NWNX_Events", "PUSH_EVENT_DATA");
    }

    // Signals an event. This will dispatch a notification to all subscribed handlers.
    // Returns TRUE if anyone was subscribed to the event, FALSE otherwise.
    public static int SignalEvent(String evt, NWObject target)
    {
        NWNX_PushArgumentObject("NWNX_Events", "SIGNAL_EVENT", target);
        NWNX_PushArgumentString("NWNX_Events", "SIGNAL_EVENT", evt);
        NWNX_CallFunction("NWNX_Events", "SIGNAL_EVENT");
        return NWNX_GetReturnValueInt("NWNX_Events", "SIGNAL_EVENT");
    }

    // Retrieves the event data for the currently executing script.
    // THIS SHOULD ONLY BE CALLED FROM WITHIN AN EVENT HANDLER.
    public static String GetEventDataString(String tag)
    {
        NWNX_PushArgumentString("NWNX_Events", "GET_EVENT_DATA", tag);
        NWNX_CallFunction("NWNX_Events", "GET_EVENT_DATA");
        return NWNX_GetReturnValueString("NWNX_Events", "GET_EVENT_DATA");
    }

    private static int GetEventDataInt(String tag)
    {
        String data = GetEventDataString(tag);
        return Integer.parseInt(data);
    }

    private static boolean GetEventDataBoolean(String tag)
    {
        int data = GetEventDataInt(tag);
        return data == 1;
    }

    private static float GetEventDataFloat(String tag)
    {
        String data = GetEventDataString(tag);
        return Float.parseFloat(data);
    }

    private static NWObject GetEventDataObject(String tag)
    {
        String data = GetEventDataString(tag);
        return NWNX_Object.StringToObject(data);
    }

    public static int OnFeatUsed_GetFeatID()
    {
        return GetEventDataInt("FEAT_ID");
    }

    public static int OnFeatUsed_GetSubFeatID()
    {
        return GetEventDataInt("SUBFEAT_ID");
    }

    public static NWObject OnFeatUsed_GetTarget()
    {
        return GetEventDataObject("TARGET_OBJECT_ID");
    }

    public static NWLocation OnFeatUsed_GetTargetLocation()
    {
        return NWScript.location(
                OnFeatUsed_GetArea(),
                NWScript.vector(OnFeatUsed_GetTargetPositionX(), OnFeatUsed_GetTargetPositionY(), OnFeatUsed_GetTargetPositionZ()),
                0.0f
        );
    }

    public static NWObject OnFeatUsed_GetArea()
    {
        return GetEventDataObject("AREA_OBJECT_ID");
    }

    public static float OnFeatUsed_GetTargetPositionX()
    {
        return GetEventDataFloat("TARGET_POSITION_X");
    }

    public static float OnFeatUsed_GetTargetPositionY()
    {
        return GetEventDataFloat("TARGET_POSITION_Y");
    }

    public static float OnFeatUsed_GetTargetPositionZ()
    {
        return GetEventDataFloat("TARGET_POSITION_Z");
    }

    public static NWObject OnItemUsed_GetItem()
    {
        return GetEventDataObject("ITEM_OBJECT_ID");
    }

    public static NWObject OnItemUsed_GetTarget()
    {
        return GetEventDataObject("TARGET_OBJECT_ID");
    }

    public static int OnItemUsed_GetItemPropertyIndex()
    {
        return GetEventDataInt("ITEM_PROPERTY_INDEX");
    }

    public static int OnItemUsed_GetValue2()
    {
        return GetEventDataInt("TEST_VALUE_2");
    }

    public static NWObject OnExamineObject_GetTarget()
    {
        return GetEventDataObject("EXAMINEE_OBJECT_ID");
    }

    public static int OnCastSpell_GetSpellID()
    {
        return GetEventDataInt("SPELL_ID");
    }

    public static int OnCastSpell_GetTargetPositionX()
    {
        return GetEventDataInt("TARGET_POSITION_X");
    }

    public static int OnCastSpell_GetTargetPositionY()
    {
        return GetEventDataInt("TARGET_POSITION_Y");
    }

    public static int OnCastSpell_GetTargetPositionZ()
    {
        return GetEventDataInt("TARGET_POSITION_Z");
    }

    public static NWObject OnCastSpell_GetTarget()
    {
        return GetEventDataObject("TARGET_OBJECT_ID");
    }

    public static int OnCastSpell_GetMultiClass()
    {
        return GetEventDataInt("MULTI_CLASS");
    }

    public static NWObject OnCastSpell_GetItem()
    {
        return GetEventDataObject("ITEM_OBJECT_ID");
    }

    public static boolean OnCastSpell_GetSpellCountered()
    {
        return GetEventDataBoolean("SPELL_COUNTERED");
    }

    public static boolean OnCastSpell_GetCounteringSpell()
    {
        return GetEventDataBoolean("COUNTERING_SPELL");
    }

    public static int OnCastSpell_GetProjectilePathType()
    {
        return GetEventDataInt("PROJECTILE_PATH_TYPE");
    }

    public static boolean OnCastSpell_IsInstantSpell()
    {
        return GetEventDataBoolean("IS_INSTANT_SPELL");
    }

    public static NWObject OnCombatRoundStart_GetTarget()
    {
        return GetEventDataObject("TARGET_OBJECT_ID");
    }

}
