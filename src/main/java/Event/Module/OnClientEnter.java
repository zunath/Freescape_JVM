package Event.Module;

import Common.Constants;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import Data.Repository.ServerConfigurationRepository;
import Entities.PlayerEntity;
import Entities.ServerConfigurationEntity;
import GameObject.PlayerGO;
import GameSystems.ActivityLoggingSystem;
import GameSystems.PlayerInitializationSystem;
import GameSystems.QuestSystem;
import GameSystems.SkillSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;

@SuppressWarnings("unused")
public class OnClientEnter implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getEnteringObject();

        // Bioware Default
        NWScript.executeScript("x3_mod_def_enter", objSelf);
        PlayerInitializationSystem.OnModuleEnter();
        SkillSystem.OnModuleEnter();
        LoadCharacter();
        ShowMOTD();
        ApplyGhostwalk();
        QuestSystem.OnClientEnter();
        ActivityLoggingSystem.OnModuleClientEnter();
    }

    private void ApplyGhostwalk()
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        NWEffect eGhostWalk = NWScript.effectCutsceneGhost();
        NWScript.applyEffectToObject(Duration.TYPE_PERMANENT, eGhostWalk, oPC, 0.0f);

    }

    private void ShowMOTD()
    {
        ServerConfigurationEntity config = ServerConfigurationRepository.GetServerConfiguration();

        final NWObject oPC = NWScript.getEnteringObject();
        final String message = ColorToken.Green() + "Welcome to " + config.getServerName() + "!\n\nMOTD:" + ColorToken.White() +  config.getMessageOfTheDay() + ColorToken.End();

        Scheduler.delay(oPC, 6500, () -> NWScript.sendMessageToPC(oPC, message));
    }

    private void LoadCharacter()
    {
        final NWObject oPC = NWScript.getEnteringObject();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());

        if(entity == null) return;

        int hp = NWScript.getCurrentHitPoints(oPC);
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
            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(damage, DamageType.MAGICAL, DamagePower.NORMAL), oPC, 0.0f);
        }

        pcGO.setIsBusy(false); // Just in case player logged out in the middle of an action.
    }
}
