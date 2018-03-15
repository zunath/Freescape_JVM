package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

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


}
