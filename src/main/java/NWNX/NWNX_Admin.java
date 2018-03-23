package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

import static NWNX.NWNX_Core.*;

public class NWNX_Admin {

    // Gets, sets, and clears the current player password.
    // Note that this password can be an empty string.
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

    // Gets the current DM password.
    // Note that this password can be an empty string.
    public static String GetDMPassword()
    {
        NWNX_CallFunction("NWNX_Administration", "GET_DM_PASSWORD");
        return NWNX_GetReturnValueString("NWNX_Administration", "GET_DM_PASSWORD");
    }

    // Sets the current DM password.
    // Note that this password can be an empty string.
    public static void SetDMPassword(String password)
    {
        NWNX_PushArgumentString("NWNX_Administration", "SET_DM_PASSWORD", password);
        NWNX_CallFunction("NWNX_Administration", "SET_DM_PASSWORD");
    }

    // Signals the server to immediately shut down.
    public static void ShutdownServer()
    {
        NWNX_CallFunction("NWNX_Administration", "SHUTDOWN_SERVER");
    }

    // Deletes the player character from the servervault
    //    bPreserveBackup - if true, it will leave the file on server,
    //                      only appending ".deleted0" to the bic filename.
    // The PC will be immediately booted from the game with a "Delete Character" message
    public static void DeletePlayerCharacter(NWObject pc, int bPreserveBackup)
    {
        NWNX_PushArgumentInt("NWNX_Administration", "DELETE_PLAYER_CHARACTER", bPreserveBackup);
        NWNX_PushArgumentObject("NWNX_Administration", "DELETE_PLAYER_CHARACTER", pc);
        NWNX_CallFunction("NWNX_Administration", "DELETE_PLAYER_CHARACTER");
    }


    // Ban a given IP - get via GetPCIPAddress()
    public static void AddBannedIP(String ip)
    {
        NWNX_PushArgumentString("NWNX_Administration", "ADD_BANNED_IP", ip);
        NWNX_CallFunction("NWNX_Administration", "ADD_BANNED_IP");
    }
    // Removes a ban on a given IP
    public static void RemoveBannedIP(String ip)
    {
        NWNX_PushArgumentString("NWNX_Administration", "REMOVE_BANNED_IP", ip);
        NWNX_CallFunction("NWNX_Administration", "REMOVE_BANNED_IP");
    }
    // Ban a given public cdkey - get via GetPCPublicCDKey
    public static void AddBannedCDKey(String key)
    {
        NWNX_PushArgumentString("NWNX_Administration", "ADD_BANNED_CDKEY", key);
        NWNX_CallFunction("NWNX_Administration", "ADD_BANNED_CDKEY");
    }
    // Removes a ban on a given CD Key
    public static void RemoveBannedCDKey(String key)
    {
        NWNX_PushArgumentString("NWNX_Administration", "REMOVE_BANNED_CDKEY", key);
        NWNX_CallFunction("NWNX_Administration", "REMOVE_BANNED_CDKEY");
    }
    // Ban a given player name - get via GetPCPlayerName
    // NOTE: Players can easily change their names
    public static void AddBannedPlayerName(String playername)
    {
        NWNX_PushArgumentString("NWNX_Administration", "ADD_BANNED_PLAYER_NAME", playername);
        NWNX_CallFunction("NWNX_Administration", "ADD_BANNED_PLAYER_NAME");
    }
    // Removes a ban a given player name
    public static void RemoveBannedPlayerName(String playername)
    {
        NWNX_PushArgumentString("NWNX_Administration", "REMOVE_BANNED_PLAYER_NAME", playername);
        NWNX_CallFunction("NWNX_Administration", "REMOVE_BANNED_PLAYER_NAME");
    }

    // Get a list of all banned IPs/Keys/names as a string
    public static String GetBannedList()
    {
        NWNX_CallFunction("NWNX_Administration", "GET_BANNED_LIST");
        return NWNX_GetReturnValueString("NWNX_Administration", "GET_BANNED_LIST");
    }
}
