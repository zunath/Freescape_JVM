package GameSystems;

import Helper.ColorToken;
import GameObject.PlayerGO;
import NWNX.*;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.Objects;

public class RadioSystem {

    // The number of channels available to players using radios.
    // Default: 10
    private final int RADIO_NUMBER_OF_CHANNELS = 10;

    // Tag and resref of the radio item
    private final String RADIO_RESREF = "reo_radio";

    // Name of the variable which determines if a radio is turned on or not.
    private final String RADIO_POWER = "RADIO_POWER";
    // Name of the variable which determines which radio a PC is currently tuned into.
    // A PC may only be tuned into one station at a time even if they have more than one
    // radio in their inventory.
    private final String RADIO_CHANNEL = "RADIO_CHANNEL";

    // The name of the variable which tracks the PC ID number the radio was turned by.
    // This is used to ensure the radios don't get turned off after a server reset,
    // as the game fires the OnAcquire event for all items on module entry. Normally this would
    // reset the radio's status but if the PC ID matches then we can ignore it.
    private final String RADIO_PC_ID_ENABLED_BY = "RADIO_PC_ID_ENABLED_BY";


    // Resref and tag of the radio NPC which handles distributing messages.
    private final String RADIO_NPC = "radio_npc";



    public void OnNWNXChat(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oDatabase = pcGO.GetDatabaseItem();
        
        int iChannel = NWScript.getLocalInt(oDatabase, RADIO_CHANNEL);

        int mode = NWNX_Chat.GetChannel();
        String messageText = NWNX_Chat.GetMessage();

        // This only matters when a PC uses the party chat channel
        if(mode != ChatChannel.NWNX_CHAT_CHANNEL_PLAYER_PARTY &&
                mode != ChatChannel.NWNX_CHAT_CHANNEL_DM_PARTY) return;

        NWNX_Chat.SkipMessage();
        NWObject oNPC = NWScript.getObjectByTag(RADIO_NPC, 0);

        // Can't send messages without a radio turned on.
        if(iChannel <= 0)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You must have a radio to communicate over party chat." + ColorToken.End());
            return;
        }
        String sSenderName = ColorToken.Custom(115, 101, 206) + "(Ch. " + iChannel + ") " + NWScript.getName(oPC, false) + ": " + ColorToken.End();

        NWObject[] oMembers = NWScript.getPCs();
        for(NWObject member : oMembers)
        {
            pcGO = new PlayerGO(member);
            NWObject oMemberDatabase = pcGO.GetDatabaseItem();
            int iMemberChannel = NWScript.getLocalInt(oMemberDatabase, RADIO_CHANNEL);

            // Message is sent to anyone if they've got a radio tuned in to the correct channel. They do not need to be
            // in the same party.
            if(iMemberChannel == iChannel || NWScript.getIsDM(member))
            {
                NWNX_Chat.SendMessage(ChatChannel.NWNX_CHAT_CHANNEL_PLAYER_TELL,
                        sSenderName + ColorToken.White() + messageText + ColorToken.End(),
                        oNPC,
                        member);
            }
        }
    }

    public void ChangeChannel(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oRadio = NWNX_Events.OnItemUsed_GetItem();
        NWObject oDatabase = pcGO.GetDatabaseItem();
        int iRadioChannel = NWScript.getLocalInt(oDatabase, RADIO_CHANNEL);
        int bPoweredOn = NWScript.getLocalInt(oRadio, RADIO_POWER);

        // Can't change channel unless the radio is turned on
        if(bPoweredOn == 0)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You must turn the radio on first." + ColorToken.End());
            return;
        }

        iRadioChannel++;

        // Can't cycle beyond the maximum number of channels
        if(iRadioChannel > RADIO_NUMBER_OF_CHANNELS)
        {
            iRadioChannel = 1;
        }

        // Mark the new channel, inform player of new channel, and update the radio's name to reflect the new channel
        NWScript.setLocalInt(oDatabase, RADIO_CHANNEL, iRadioChannel);
        NWScript.sendMessageToPC(oPC, ColorToken.Purple() + "Radio Channel: " + iRadioChannel + ColorToken.End());
        UpdateItemName(oRadio);
    }

    public void TogglePower(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oRadio = NWNX_Events.OnItemUsed_GetItem();
        NWObject oDatabase = pcGO.GetDatabaseItem();
        int iRadioChannel = NWScript.getLocalInt(oDatabase, RADIO_CHANNEL);
        int bPoweredOn = NWScript.getLocalInt(oRadio, RADIO_POWER);
        String sUUID = pcGO.getUUID();

        // Another radio is already turned on. Can't turn on another.
        if(iRadioChannel > 0 && bPoweredOn == 0)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "Another radio is already turned on. You may only have one radio turned on at a time." + ColorToken.End());
            return;
        }

        // It's powered on right now, but we're turning it off. Remove variables from the owner and the radio itself
        if(bPoweredOn == 1)
        {
            NWScript.deleteLocalInt(oDatabase, RADIO_CHANNEL);
            NWScript.deleteLocalInt(oRadio, RADIO_POWER);
            NWScript.deleteLocalInt(oRadio, RADIO_PC_ID_ENABLED_BY);
            NWScript.sendMessageToPC(oPC, ColorToken.Purple() + "Radio powered off." + ColorToken.End());
        }
        // It's powered off right now, and we're turning it on. Add variables to the owner and the radio itself
        else
        {
            NWScript.setLocalInt(oDatabase, RADIO_CHANNEL, 1);
            NWScript.setLocalInt(oRadio, RADIO_POWER, 1);
            NWScript.setLocalString(oRadio, RADIO_PC_ID_ENABLED_BY, sUUID);
            NWScript.sendMessageToPC(oPC, ColorToken.Purple() + "Radio powered on." + ColorToken.End());
            NWScript.sendMessageToPC(oPC, ColorToken.Purple() + "Radio Channel: 1" + ColorToken.End());
        }
        UpdateItemName(oRadio);
    }

    public void OnModuleUnacquire()
    {
        NWObject oPC = NWScript.getModuleItemLostBy();
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oRadio = NWScript.getModuleItemLost();
        String sResref = NWScript.getResRef(oRadio);

        if(Objects.equals(sResref, RADIO_RESREF))
        {
            NWObject oDatabase = pcGO.GetDatabaseItem();
            int bPoweredOn = NWScript.getLocalInt(oRadio, RADIO_POWER);
            if(bPoweredOn == 1)
            {
                NWScript.deleteLocalInt(oDatabase, RADIO_CHANNEL);
                NWScript.deleteLocalInt(oRadio, RADIO_POWER);
                UpdateItemName(oRadio);
            }
        }
    }

    public void OnModuleAcquire()
    {
        NWObject oPC = NWScript.getModuleItemAcquiredBy();
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oDatabase = pcGO.GetDatabaseItem();
        NWObject oRadio = NWScript.getModuleItemAcquired();
        String sResref = NWScript.getResRef(oRadio);
        String sUUID = pcGO.getUUID();
        String sRadioPCID = NWScript.getLocalString(oRadio, RADIO_PC_ID_ENABLED_BY);

        if(Objects.equals(sResref, RADIO_RESREF))
        {
            int bPoweredOn = NWScript.getLocalInt(oRadio, RADIO_POWER);

            // The radio must be turned on and the person who turned it on must not be
            // the current owner. I.E: When the server resets, the OnAcquire event is fired
            // for all items. This check prevents the radio's status from being reset when
            // that happens.
            if(bPoweredOn == 1 && !Objects.equals(sUUID, sRadioPCID))
            {
                NWScript.deleteLocalInt(oDatabase, RADIO_CHANNEL);
                NWScript.deleteLocalInt(oRadio, RADIO_POWER);
                UpdateItemName(oRadio);
            }
        }
    }

    private void UpdateItemName(NWObject radio)
    {
        NWObject oPC = NWScript.getItemPossessor(radio);
        PlayerGO pcGO = new PlayerGO(oPC);

        NWObject oDatabase = pcGO.GetDatabaseItem();
        String sName = NWScript.getName(radio, true);
        String sNewName = "";

        int iChannel = NWScript.getLocalInt(oDatabase, RADIO_CHANNEL);
        if(iChannel > 0)
        {
            sNewName = sName + ColorToken.Custom(0, 255, 0) + " (Channel " + iChannel + ")";
        }

        // Update item name
        NWScript.setName(radio, sNewName);
    }


}
