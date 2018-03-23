package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.*;

import static NWNX.NWNX_Core.*;

public class NWNX_Item {

    private static final String NWNX_Item = "NWNX_Item";

    public static void SetWeight(NWObject oItem, int w)
    {
        String sFunc = "SetWeight";

        NWNX_PushArgumentInt(NWNX_Item, sFunc, w);
        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);
    }


    public static void SetGoldPieceValue(NWObject oItem, int g)
    {
        String sFunc = "SetGoldPieceValue";

        NWNX_PushArgumentInt(NWNX_Item, sFunc, g);
        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);
    }


    public static void SetAddGoldPieceValue(NWObject oItem, int g)
    {
        String sFunc = "SetAddGoldPieceValue";

        NWNX_PushArgumentInt(NWNX_Item, sFunc, g);
        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);
    }

    public static int GetBaseGoldPieceValue(NWObject oItem)
    {
        String sFunc = "GetBaseGoldPieceValue";

        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Item, sFunc);
    }

    public static int GetAddGoldPieceValue(NWObject oItem)
    {
        String sFunc = "GetAddGoldPieceValue";

        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Item, sFunc);
    }

    public static void SetBaseItemType(NWObject oItem, int nBaseItem)
    {
        String sFunc = "SetBaseItemType";

        NWNX_PushArgumentInt(NWNX_Item, sFunc, nBaseItem);
        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);
    }

    public static void SetArmorColor(NWObject oItem, int nIndex, int nColor)
    {
        if(nIndex>=ItemApprArmorColor.LEATHER1 &&
                nIndex<=ItemApprArmorColor.METAL2 &&
                nColor>=0 && nColor<=175)
        {
            SetItemAppearance(oItem, nIndex, nColor);
        }
    }

    public static void SetWeaponColor(NWObject oItem, int nIndex, int nColor)
    {
        if(nIndex>=ItemApprWeaponColor.BOTTOM &&
                nIndex<=ItemApprWeaponColor.TOP &&
                nColor>=1 && nColor<=4)
        {
            SetItemAppearance(oItem, nIndex, nColor);
        }
    }

    public static void SetWeaponAppearance(NWObject oItem, int nIndex, int nValue)
    {
        if(nIndex>=ItemApprWeaponModel.BOTTOM &&
                nIndex<=ItemApprWeaponModel.TOP &&
                nValue>=0 && nValue<=255)
        {
            SetItemAppearance(oItem, nIndex + 6, nValue);
        }
    }

    public static void SetArmorAppearance(NWObject oItem, int nIndex, int nValue)
    {
        if(nIndex>=ItemApprArmorModel.RFOOT &&
                nIndex<=ItemApprArmorModel.ROBE &&
                nValue>=0 && nValue<=255)
        {
            SetItemAppearance(oItem, nIndex + 9, nValue);
        }
    }

    public static void SetItemAppearance(NWObject oItem, int nIndex, int nValue)
    {
        String sFunc = "SetItemAppearance";

        NWNX_PushArgumentInt(NWNX_Item, sFunc, nValue);
        NWNX_PushArgumentInt(NWNX_Item, sFunc, nIndex);
        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);

    }

    public static String GetEntireItemAppearance(NWObject oItem)
    {
        String sFunc = "GetEntireItemAppearance";

        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);
        return NWNX_GetReturnValueString(NWNX_Item, sFunc);
    }

    public static void RestoreItemAppearance(NWObject oItem, String sApp)
    {
        String sFunc = "RestoreItemAppearance";

        NWNX_PushArgumentString(NWNX_Item, sFunc, sApp);
        NWNX_PushArgumentObject(NWNX_Item, sFunc, oItem);

        NWNX_CallFunction(NWNX_Item, sFunc);
    }

}
