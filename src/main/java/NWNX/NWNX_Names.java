package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

import static NWNX.NWNX_Core.*;

public class NWNX_Names {


    // Sets the dynamic name of target to name.
    // Optionally, observer can be set so the name only changes for them (must be PC).
    public static void NWNX_Names_SetName(NWObject target, String name, NWObject observer)
    {
        NWNX_PushArgumentObject("NWNX_Names", "SET_NAME", target);
        NWNX_PushArgumentString("NWNX_Names", "SET_NAME", name);
        NWNX_PushArgumentObject("NWNX_Names", "SET_NAME", observer);
        NWNX_CallFunction("NWNX_Names", "SET_NAME");
    }

    // Gets the dynamic name of target.
    // Optionally, observer can be set to get the name as perceived by observer (must be a PC).
    public static String NWNX_Names_GetName(NWObject target, NWObject observer)
    {
        NWNX_PushArgumentObject("NWNX_Names", "GET_NAME", target);
        NWNX_PushArgumentObject("NWNX_Names", "GET_NAME", observer);
        NWNX_CallFunction("NWNX_Names", "GET_NAME");
        return NWNX_GetReturnValueString("NWNX_Names", "GET_NAME");
    }


}
