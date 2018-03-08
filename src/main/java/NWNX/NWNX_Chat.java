package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

import static NWNX.NWNX_Core.*;

public class NWNX_Chat {

    // Sends a chat message. Channel is a NWNX_* constant.
    // If no target is provided, then it broadcasts to all eligible targets.
    // Returns TRUE if successful, FALSE otherwise.
    public static int SendMessage(int channel, String message, NWObject sender, NWObject target)
    {
        NWNX_PushArgumentObject("NWNX_Chat", "SEND_MESSAGE", target);
        NWNX_PushArgumentObject("NWNX_Chat", "SEND_MESSAGE", sender);
        NWNX_PushArgumentString("NWNX_Chat", "SEND_MESSAGE", message);
        NWNX_PushArgumentInt("NWNX_Chat", "SEND_MESSAGE", channel);
        NWNX_CallFunction("NWNX_Chat", "SEND_MESSAGE");
        return NWNX_GetReturnValueInt("NWNX_Chat", "SEND_MESSAGE");
    }

    // Registers the script which receives all chat messages.
    // If a script was previously registered, this one will take over.
    public static void RegisterChatScript(String script)
    {
        NWNX_PushArgumentString("NWNX_Chat", "REGISTER_CHAT_SCRIPT", script);
        NWNX_CallFunction("NWNX_Chat", "REGISTER_CHAT_SCRIPT");
    }

    // Skips the message.
    // Must be called from an chat or system script handler.
    public static void SkipMessage()
    {
        NWNX_CallFunction("NWNX_Chat", "SKIP_MESSAGE");
    }

    // Gets the channel. Channel is a NWNX_* constant.
    // Must be called from an chat or system script handler.
    public static int GetChannel()
    {
        NWNX_CallFunction("NWNX_Chat", "GET_CHANNEL");
        return NWNX_GetReturnValueInt("NWNX_Chat", "GET_CHANNEL");
    }

    // Gets the message.
    // Must be called from an chat or system script handler.
    public static String GetMessage()
    {
        NWNX_CallFunction("NWNX_Chat", "GET_MESSAGE");
        return NWNX_GetReturnValueString("NWNX_Chat", "GET_MESSAGE");
    }

    // Gets the sender.
    // Must be called from an chat or system script handler.
    public static NWObject GetSender()
    {
        NWNX_CallFunction("NWNX_Chat", "GET_SENDER");
        return NWNX_GetReturnValueObject("NWNX_Chat", "GET_SENDER");
    }

    // Gets the target. May be OBJECT_INVALID if no target.
    // Must be called from an chat or system script handler.
    public static NWObject GetTarget()
    {
        NWNX_CallFunction("NWNX_Chat", "GET_TARGET");
        return NWNX_GetReturnValueObject("NWNX_Chat", "GET_TARGET");
    }
}
