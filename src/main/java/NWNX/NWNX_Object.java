package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWVector;

import static NWNX.NWNX_Core.*;

public class NWNX_Object {
    private static final String NWNX_Object = "NWNX_Object";


    public static int GetLocalVariableCount(NWObject obj)
    {
        String sFunc = "GetLocalVariableCount";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);
        NWNX_CallFunction(NWNX_Object, sFunc);

        return NWNX_GetReturnValueInt(NWNX_Object, sFunc);
    }

    public static LocalVariable GetLocalVariable(NWObject obj, int index)
    {
        String sFunc = "GetLocalVariable";

        NWNX_PushArgumentInt(NWNX_Object, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);
        NWNX_CallFunction(NWNX_Object, sFunc);

        LocalVariable var = new LocalVariable();
        var.key  = NWNX_GetReturnValueString(NWNX_Object, sFunc);
        var.type = NWNX_GetReturnValueInt(NWNX_Object, sFunc);
        return var;
    }

    public static NWObject StringToObject(String id)
    {
        String sFunc = "StringToObject";

        NWNX_PushArgumentString(NWNX_Object, sFunc, id);
        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueObject(NWNX_Object, sFunc);
    }

    public static String GetEventHandler(NWObject obj, int handler)
    {
        String sFunc = "GetEventHandler";

        NWNX_PushArgumentInt(NWNX_Object, sFunc, handler);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);
        NWNX_CallFunction(NWNX_Object, sFunc);

        return NWNX_GetReturnValueString(NWNX_Object, sFunc);
    }

    public static void SetEventHandler(NWObject obj, int handler, String script)
    {
        String sFunc = "SetEventHandler";

        NWNX_PushArgumentString(NWNX_Object, sFunc, script);
        NWNX_PushArgumentInt(NWNX_Object, sFunc, handler);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);
        NWNX_CallFunction(NWNX_Object, sFunc);

    }

    public static void SetPosition(NWObject obj, NWVector pos)
    {
        String sFunc = "SetPosition";

        NWNX_PushArgumentFloat(NWNX_Object, sFunc, pos.getX());
        NWNX_PushArgumentFloat(NWNX_Object, sFunc, pos.getY());
        NWNX_PushArgumentFloat(NWNX_Object, sFunc, pos.getZ());
        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);
        NWNX_CallFunction(NWNX_Object, sFunc);

    }

    public static void SetCurrentHitPoints(NWObject creature, int hp)
    {
        String sFunc = "SetCurrentHitPoints";

        NWNX_PushArgumentInt(NWNX_Object, sFunc, hp);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, creature);

        NWNX_CallFunction(NWNX_Object, sFunc);
    }

    public static void SetMaxHitPoints(NWObject creature, int hp)
    {
        String sFunc = "SetMaxHitPoints";

        NWNX_PushArgumentInt(NWNX_Object, sFunc, hp);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, creature);

        NWNX_CallFunction(NWNX_Object, sFunc);
    }
    public static String GetPortrait(NWObject creature)
    {
        String sFunc = "GetPortrait";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, creature);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueString(NWNX_Object, sFunc);
    }

    public static void SetPortrait(NWObject creature, String portrait)
    {
        String sFunc = "SetPortrait";

        NWNX_PushArgumentString(NWNX_Object, sFunc, portrait);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, creature);

        NWNX_CallFunction(NWNX_Object, sFunc);
    }

    public static String Serialize(NWObject obj)
    {
        String sFunc = "Serialize";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueString(NWNX_Object, sFunc);
    }

    public static NWObject Deserialize(String serialized)
    {
        String sFunc = "Deserialize";

        NWNX_PushArgumentString(NWNX_Object, sFunc, serialized);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueObject(NWNX_Object, sFunc);
    }


    public static String GetDialogResref(NWObject obj)
    {
        String sFunc = "GetDialogResref";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueString(NWNX_Object, sFunc);
    }


    public static void NWNX_Object_SetAppearance(NWObject obj, int app)
    {
        String sFunc = "SetAppearance";

        NWNX_PushArgumentInt(NWNX_Object, sFunc, app);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);

        NWNX_CallFunction(NWNX_Object, sFunc);
    }

    public static int NWNX_Object_GetAppearance(NWObject obj)
    {
        String sFunc = "GetAppearance";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Object, sFunc);
    }
}
