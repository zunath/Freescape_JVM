package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWVector;

import static NWNX.NWNX_Core.*;

public class NWNX_Object {
    private static final String NWNX_Object = "NWNX_Object";

    // Gets the count of all local variables on the provided object.
    public static int GetLocalVariableCount(NWObject obj)
    {
        String sFunc = "GetLocalVariableCount";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);
        NWNX_CallFunction(NWNX_Object, sFunc);

        return NWNX_GetReturnValueInt(NWNX_Object, sFunc);
    }

    //Gets the local variable at the provided index of the provided object.
    // Index bounds: 0 >= index < GetLocalVariableCount(obj).
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

    // Returns an NWObject from the provided NWObject ID.
    // This is the counterpart to ObjectToString.
    public static NWObject StringToObject(String id)
    {
        String sFunc = "StringToObject";

        NWNX_PushArgumentString(NWNX_Object, sFunc, id);
        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueObject(NWNX_Object, sFunc);
    }

    // Set the provided object's position to the provided vector.
    public static void SetPosition(NWObject obj, NWVector pos)
    {
        String sFunc = "SetPosition";

        NWNX_PushArgumentFloat(NWNX_Object, sFunc, pos.getX());
        NWNX_PushArgumentFloat(NWNX_Object, sFunc, pos.getY());
        NWNX_PushArgumentFloat(NWNX_Object, sFunc, pos.getZ());
        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);
        NWNX_CallFunction(NWNX_Object, sFunc);

    }

    // Sets the provided object's current hit points to the provided value.
    public static void SetCurrentHitPoints(NWObject creature, int hp)
    {
        String sFunc = "SetCurrentHitPoints";

        NWNX_PushArgumentInt(NWNX_Object, sFunc, hp);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, creature);

        NWNX_CallFunction(NWNX_Object, sFunc);
    }

    // Set object's maximum hit points; will not work on PCs.
    public static void SetMaxHitPoints(NWObject creature, int hp)
    {
        String sFunc = "SetMaxHitPoints";

        NWNX_PushArgumentInt(NWNX_Object, sFunc, hp);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, creature);

        NWNX_CallFunction(NWNX_Object, sFunc);
    }

    // Get the name of the portrait NWObject is using.
    public static String GetPortrait(NWObject creature)
    {
        String sFunc = "GetPortrait";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, creature);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueString(NWNX_Object, sFunc);
    }

    // Set the portrait NWObject is using. The portrait String must be no more than 15 characters long.
    public static void SetPortrait(NWObject creature, String portrait)
    {
        String sFunc = "SetPortrait";

        NWNX_PushArgumentString(NWNX_Object, sFunc, portrait);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, creature);

        NWNX_CallFunction(NWNX_Object, sFunc);
    }


    // Serialize the full NWObject (including locals, inventory, etc) to base64 string
    // Only works on Creatures and Items currently.
    public static String Serialize(NWObject obj)
    {
        String sFunc = "Serialize";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueString(NWNX_Object, sFunc);
    }

    // Deserialize the object. The NWObject will be created outside of the world and
    // needs to be manually positioned at a location/inventory.
    public static NWObject Deserialize(String serialized)
    {
        String sFunc = "Deserialize";

        NWNX_PushArgumentString(NWNX_Object, sFunc, serialized);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueObject(NWNX_Object, sFunc);
    }


    // Returns the dialog resref of the object.
    public static String GetDialogResref(NWObject obj)
    {
        String sFunc = "GetDialogResref";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueString(NWNX_Object, sFunc);
    }

    // Set obj's appearance. Will not update for PCs until they
    // re-enter the area.
    public static void SetAppearance(NWObject obj, int app)
    {
        String sFunc = "SetAppearance";

        NWNX_PushArgumentInt(NWNX_Object, sFunc, app);
        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);

        NWNX_CallFunction(NWNX_Object, sFunc);
    }

    // Get obj's appearance
    public static int GetAppearance(NWObject obj)
    {
        String sFunc = "GetAppearance";

        NWNX_PushArgumentObject(NWNX_Object, sFunc, obj);

        NWNX_CallFunction(NWNX_Object, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Object, sFunc);
    }

}
