package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class NWNX_DMActions_Old {
    
    public static void SetDMActionScript(int nAction, String sScript) {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!DMACTIONS!SET_ACTION_SCRIPT", nAction + ":" + sScript);
        NWScript.deleteLocalString(NWObject.MODULE, "NWNX!DMACTIONS!SET_ACTION_SCRIPT");
    }

    public static void PreventDMAction() {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!DMACTIONS!PREVENT", "1");
        NWScript.deleteLocalString(NWObject.MODULE, "NWNX!DMACTIONS!PREVENT");
    }

    public static int nGetDMAction_Param(boolean bSecond) {
        String sNth = bSecond?"2":"1";
        NWScript.setLocalString(NWObject.MODULE, "NWNX!DMACTIONS!GETPARAM_" + sNth, "                ");
        String sVal = NWScript.getLocalString(NWObject.MODULE, "NWNX!DMACTIONS!GETPARAM_" + sNth);
        NWScript.deleteLocalString(NWObject.MODULE, "NWNX!DMACTIONS!GETPARAM_" + sNth);
        return NWScript.stringToInt(sVal);
    }

    public static NWObject oGetDMAction_Target(boolean bSecond) {
        String sNth = bSecond?"2":"1";
        return NWScript.getLocalObject(NWObject.MODULE, "NWNX!DMACTIONS!TARGET_" + sNth);
    }

}
