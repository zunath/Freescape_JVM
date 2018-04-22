package Event.Module;

import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import Data.Repository.ServerConfigurationRepository;
import Entities.PlayerEntity;
import Entities.ServerConfigurationEntity;
import GameObject.PlayerGO;
import GameSystems.*;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("unused")
public class OnClientEnter implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = getEnteringObject();

        // Bioware Default
        executeScript("x3_mod_def_enter", objSelf);
        PlayerInitializationSystem.OnModuleEnter();
        SkillSystem.OnModuleEnter();
        LoadCharacter();
        ShowMOTD();
        ApplyGhostwalk();
        QuestSystem.OnClientEnter();
        ActivityLoggingSystem.OnModuleClientEnter();
        ApplyScriptEvents(oPC);
        MapPinSystem.OnClientEnter();
    }

    private void ApplyGhostwalk()
    {
        NWObject oPC = getEnteringObject();

        if(!getIsPC(oPC) || getIsDM(oPC)) return;

        NWEffect eGhostWalk = effectCutsceneGhost();
        eGhostWalk = tagEffect(eGhostWalk, "GHOST_WALK");
        applyEffectToObject(Duration.TYPE_PERMANENT, eGhostWalk, oPC, 0.0f);

    }

    private void ShowMOTD()
    {
        ServerConfigurationEntity config = ServerConfigurationRepository.GetServerConfiguration();

        final NWObject oPC = getEnteringObject();
        final String message = ColorToken.Green() + "Welcome to " + config.getServerName() + "!\n\nMOTD:" + ColorToken.White() +  config.getMessageOfTheDay() + ColorToken.End();

        Scheduler.delay(oPC, 6500, () -> sendMessageToPC(oPC, message));
    }

    private void LoadCharacter()
    {
        final NWObject oPC = getEnteringObject();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());

        if(entity == null) return;

        int hp = getCurrentHitPoints(oPC);
        int damage;
        if(entity.getHitPoints() < 0)
        {
            damage = hp + Math.abs(entity.getHitPoints());
        }
        else
        {
            damage = hp - entity.getHitPoints();
        }

        if(damage != 0)
        {
            applyEffectToObject(DurationType.INSTANT, effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oPC, 0.0f);
        }

        pcGO.setIsBusy(false); // Just in case player logged out in the middle of an action.
    }

    private void ApplyScriptEvents(NWObject oPC)
    {
        // As of 2018-03-28 only the OnDialogue, OnHeartbeat, and OnUserDefined events fire for PCs.
        // The rest are included here for completeness sake.

        //setEventScript(oPC, EventScript.CREATURE_ON_BLOCKED_BY_DOOR, "pc_on_blocked");
        //setEventScript(oPC, EventScript.CREATURE_ON_DAMAGED, "pc_on_damaged");
        //setEventScript(oPC, EventScript.CREATURE_ON_DEATH, "pc_on_death");
        setEventScript(oPC, EventScript.CREATURE_ON_DIALOGUE, "default");
        //setEventScript(oPC, EventScript.CREATURE_ON_DISTURBED, "pc_on_disturb");
        //setEventScript(oPC, EventScript.CREATURE_ON_END_COMBATROUND, "pc_on_endround");
        setEventScript(oPC, EventScript.CREATURE_ON_HEARTBEAT, "pc_on_heartbeat");
        //setEventScript(oPC, EventScript.CREATURE_ON_MELEE_ATTACKED, "pc_on_attacked");
        //setEventScript(oPC, EventScript.CREATURE_ON_NOTICE, "pc_on_notice");
        //setEventScript(oPC, EventScript.CREATURE_ON_RESTED, "pc_on_rested");
        //setEventScript(oPC, EventScript.CREATURE_ON_SPAWN_IN, "pc_on_spawn");
        //setEventScript(oPC, EventScript.CREATURE_ON_SPELLCASTAT, "pc_on_spellcast");
        setEventScript(oPC, EventScript.CREATURE_ON_USER_DEFINED_EVENT, "pc_on_user");
    }

}
