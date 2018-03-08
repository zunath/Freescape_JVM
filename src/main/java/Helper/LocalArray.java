package Helper;

import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class LocalArray {

    public static NWObject GetLocalArrayObject(NWObject obj, String arrayVariableName, int index)
    {
        return NWScript.getLocalObject(obj, arrayVariableName + index);
    }

    public static void SetLocalArrayObject(NWObject obj, String arrayVariableName, int index, NWObject value)
    {
        NWScript.setLocalObject(obj, arrayVariableName + index, value);
    }

    public static NWLocation GetLocalArrayLocation(NWObject obj, String arrayVariableName, int index)
    {
        return NWScript.getLocalLocation(obj, arrayVariableName + index);
    }

    public static void SetLocalArrayLocation(NWObject obj, String arrayVariableName, int index, NWLocation lLocation)
    {
        NWScript.setLocalLocation(obj, arrayVariableName + index, lLocation);
    }

    public static int GetLocalArrayInt(NWObject oObject, String sVarName, int iIndex)
    {
        return NWScript.getLocalInt(oObject, sVarName + iIndex);
    }

    public static String GetLocalArrayString(NWObject oObject, String sVarName, int iIndex)
    {
        return NWScript.getLocalString(oObject, sVarName + iIndex);
    }

    public static void SetLocalArrayInt(NWObject oObject, String sVarName, int iIndex, int iValue)
    {
        NWScript.setLocalInt(oObject, sVarName + iIndex, iValue);
    }

    public static void SetLocalArrayString(NWObject oObject, String sVarName, int iIndex, String sValue)
    {
        NWScript.setLocalString(oObject, sVarName + iIndex, sValue);
    }

    public static void DeleteLocalArrayInt(NWObject oObject, String sVarName, int iIndex)
    {
        NWScript.deleteLocalInt(oObject, sVarName + iIndex);
    }

    public static void DeleteLocalArrayString(NWObject oObject, String sVarName, int iIndex)
    {
        NWScript.deleteLocalString(oObject, sVarName + iIndex);
    }

    public static void DeleteLocalArrayObject(NWObject oObject, String sVarName, int iIndex)
    {
        NWScript.deleteLocalObject(oObject, sVarName + iIndex);
    }
}
