package Helper;

import NWNX.*;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class LocalVariableHelper {

    public static void CopyVariables(NWObject oSource, NWObject oCopy)
    {
        int variableCount = NWNX_Object.GetLocalVariableCount(oSource);
        for(int variableIndex = 0; variableIndex < variableCount-1; variableIndex++)
        {
            LocalVariable stCurVar = NWNX_Object.GetLocalVariable(oSource, variableIndex);

            switch(stCurVar.type)
            {
                case VariableType.IntVar:
                    NWScript.setLocalInt(oCopy, stCurVar.key, NWScript.getLocalInt(oSource, stCurVar.key));
                    break;
                case VariableType.FloatVar:
                    NWScript.setLocalFloat(oCopy, stCurVar.key, NWScript.getLocalFloat(oSource, stCurVar.key));
                    break;
                case VariableType.StringVar:
                    NWScript.setLocalString(oCopy, stCurVar.key, NWScript.getLocalString(oSource, stCurVar.key));
                    break;
                case VariableType.ObjectVar:
                    NWScript.setLocalObject(oCopy, stCurVar.key, NWScript.getLocalObject(oSource, stCurVar.key));
                    break;
                case VariableType.LocationVar:
                    NWScript.setLocalLocation(oCopy, stCurVar.key, NWScript.getLocalLocation(oSource, stCurVar.key));
                    break;
            }
        }
    }
}
