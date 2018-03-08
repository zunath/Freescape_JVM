package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

import static NWNX.NWNX_Core.*;

public class NWNX_Admin {

    public static String GetPlayerPassword()
    {
        NWNX_CallFunction("NWNX_Administration", "GET_PLAYER_PASSWORD");
        return NWNX_GetReturnValueString("NWNX_Administration", "GET_PLAYER_PASSWORD");
    }

    public static void SetPlayerPassword(String password)
    {
        NWNX_PushArgumentString("NWNX_Administration", "SET_PLAYER_PASSWORD", password);
        NWNX_CallFunction("NWNX_Administration", "SET_PLAYER_PASSWORD");
    }

    public static void ClearPlayerPassword()
    {
        NWNX_CallFunction("NWNX_Administration", "CLEAR_PLAYER_PASSWORD");
    }

    public static String GetDMPassword()
    {
        NWNX_CallFunction("NWNX_Administration", "GET_DM_PASSWORD");
        return NWNX_GetReturnValueString("NWNX_Administration", "GET_DM_PASSWORD");
    }

    public static void SetDMPassword(String password)
    {
        NWNX_PushArgumentString("NWNX_Administration", "SET_DM_PASSWORD", password);
        NWNX_CallFunction("NWNX_Administration", "SET_DM_PASSWORD");
    }

    public static void ShutdownServer()
    {
        NWNX_CallFunction("NWNX_Administration", "SHUTDOWN_SERVER");
    }

    public static void BootPCWithMessage(NWObject pc, int strref)
    {
        NWNX_PushArgumentInt("NWNX_Administration", "BOOT_PC_WITH_MESSAGE", strref);
        NWNX_PushArgumentObject("NWNX_Administration", "BOOT_PC_WITH_MESSAGE", pc);
        NWNX_CallFunction("NWNX_Administration", "BOOT_PC_WITH_MESSAGE");
    }

}
