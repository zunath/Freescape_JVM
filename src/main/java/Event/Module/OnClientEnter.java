package Event.Module;

import Common.Constants;
import Data.Repository.ServerConfigurationRepository;
import Entities.PlayerEntity;
import Entities.ServerConfigurationEntity;
import Enumerations.CustomClass;
import GameSystems.*;
import Helper.ColorToken;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import NWNX.NWNX_Creature;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

@SuppressWarnings("unused")
public class OnClientEnter implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        RadioSystem radioSystem = new RadioSystem();
        NWObject oPC = NWScript.getEnteringObject();

        // Bioware Default
        NWScript.executeScript("x3_mod_def_enter", objSelf);
        InitializeNewCharacter();
        LoadCharacter();
        ShowMOTD();
        ApplyGhostwalk();
        ProfessionSystem.OnModuleEnter();
        ProgressionSystem.OnModuleEnter();
        QuestSystem.OnClientEnter();

        // TODO: Fix this call for EE. Appears to be some problem with the Bioware database in Docker. Probably need to get rid of the dependency.
        //NWScript.executeScript("dmfi_onclienter", objSelf);
        ActivityLoggingSystem.OnModuleClientEnter();

        // Swap existing characters over to the "Standard" class.
        if(NWScript.getIsPC(oPC) && !NWScript.getIsDM(oPC) && !NWScript.getIsDMPossessed(oPC))
        {
            NWNX_Creature.SetClassByPosition(oPC, 0, CustomClass.Standard);
        }
    }

    private void ApplyGhostwalk()
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        NWEffect eGhostWalk = NWScript.effectCutsceneGhost();
        NWScript.applyEffectToObject(Duration.TYPE_PERMANENT, eGhostWalk, oPC, 0.0f);

    }

    private void InitializeNewCharacter()
    {
        final NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oDatabase = pcGO.GetDatabaseItem();

        boolean missingStringID = NWScript.getLocalString(oDatabase, Constants.PCIDNumberVariable).equals("");

        if(oDatabase == NWObject.INVALID || missingStringID)
        {
            pcGO.destroyAllEquippedItems();
            pcGO.destroyAllInventoryItems(true);

            NWScript.createItemOnObject(Constants.PCDatabaseTag, oPC, 1, "");

            Scheduler.assign(oPC, () -> {
                NWScript.takeGoldFromCreature(NWScript.getGold(oPC), oPC, true);
                NWScript.giveGoldToCreature(oPC, 10);
            });

            NWObject bread = NWScript.createItemOnObject("food_bread", oPC, 1, "");
            NWScript.setName(bread, "Starting Bread");
            NWScript.setItemCursedFlag(bread, true);
            NWScript.createItemOnObject("combat_knife", oPC, 1, "");

            NWObject darts = NWScript.createItemOnObject("nw_wthdt001", oPC, 50, ""); // 50x Dart
            NWScript.setName(darts, "Starting Darts");
            NWScript.setItemCursedFlag(darts, true);

            Scheduler.assign(oPC, () -> {
                NWObject oClothes = NWScript.createItemOnObject("starting_shirt", oPC, 1, "");
                NWScript.actionEquipItem(oClothes, InventorySlot.CHEST);
            });

            // Save to database
            PlayerRepository repo = new PlayerRepository();
            PlayerEntity entity = pcGO.createEntity();
            repo.save(entity);

            ProgressionSystem.InitializePlayer(oPC);
            //NWNX_Funcs_Old.SetRawQuickBarSlot(oPC, "1 4 0 1116 0");
            Scheduler.delay(oPC, 1000, () -> NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(999), oPC, 0.0f));
        }
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
