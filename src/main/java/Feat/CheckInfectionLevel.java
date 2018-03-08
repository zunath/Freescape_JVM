package Feat;

import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class CheckInfectionLevel implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {

        NWObject oTarget = NWScript.getSpellTargetObject();

        if(!NWScript.getIsPC(oTarget) || NWScript.getIsDM(oTarget))
        {
            NWScript.sendMessageToPC(oPC, "You cannot check the infection status of that object.");
            return;
        }

        PlayerGO targetGO = new PlayerGO(oTarget);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(targetGO.getUUID());
        int infection = entity.getCurrentInfection();
        String name = NWScript.getName(oTarget, false);
        String message;

        if(infection <= 0)
        {
            message = "This person looks to be in perfect health. They don't look infected at all.";
        }
        else if(infection <= 10)
        {
            message = "This person looks to be in generally good health despite some scuffs and bruises. They don't look very sick, if at all.";
        }
        else if(infection <= 20)
        {
            message = "Although this person may have come into contact with the infection, they still look to be in overall moderate health. It's safe to say this person isn't that infected.";
        }
        else if(infection <= 30)
        {
            message = "They look like they are in moderate health, but occasionally they seem to look a little more pale and sickly than usual. This person may want to start a course of antibiotics or antivirals";
        }
        else if(infection <= 40)
        {
            message = "Their health seems to be waning a bit. They look pale and will have some infected bite marks whenever their skin is exposed. They should begin a course of medication very soon - before it gets worse.";
        }
        else if(infection <= 50)
        {
            message = "This person's health is starting to go. They are almost always pale and sickly. You occasionally see this person getting the chills. They need medical attention very soon.";
        }
        else if(infection <= 60)
        {
            message = "They look to be in poor health. The infection is starting to take over. This individual shivers at chills and their skin is always pale. There is a purple hue around all exposed bite marks. They need some medicine immediately or they may be in danger of turning.";
        }
        else if(infection <= 70)
        {
            message = "This person is in very poor health. They are always pale and sick. Their stamina seems to be drained and they look very tired. They are also warm to the touch which indicates a fever. All bite marks appear brown and purple which indicates major infection. They need medical attention immediately.";
        }
        else if(infection <= 80)
        {
            message = "This person is in extremely bad health. They are very slow to react and their movements are shaky and inaccurate. They look very pale and appear to be sweating. They need to have medical attention immediately - otherwise they will turn very soon.";
        }
        else if(infection <= 90)
        {
            message = "This person is beyond extremely bad health. They appear to be in immediate danger of turning. Sweating, nausea, chills, fever, and poor stamina befall this individual. It's clear this person needs help now or they will turn.";
        }
        else if(infection <= 100)
        {
            message = "There is absolutely no question. This person is in EXTREME danger of turning. It appears that they will turn in a matter of a few minutes. Any attention to save this person needs to be applied now or they are a goner for sure!";
        }
        else
        {
            message = "It's impossible to tell how infected this individual looks.";
        }


        NWScript.sendMessageToPC(oPC, name + ": " + message);
    }
}
