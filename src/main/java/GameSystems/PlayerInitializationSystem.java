package GameSystems;

import Common.Constants;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import NWNX.*;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Class;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.Feat;

import static org.nwnx.nwnx2.jvm.NWScript.*;
import static org.nwnx.nwnx2.jvm.NWScript.getName;

public class PlayerInitializationSystem {

    public static void OnModuleEnter()
    {
        final NWObject oPC = getEnteringObject();

        if(!getIsPC(oPC) || getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oDatabase = pcGO.GetDatabaseItem();

        boolean missingStringID = getLocalString(oDatabase, Constants.PCIDNumberVariable).equals("");

        if(oDatabase == NWObject.INVALID || missingStringID)
        {
            pcGO.destroyAllInventoryItems(true);

            createItemOnObject(Constants.PCDatabaseTag, oPC, 1, "");
            createItemOnObject("open_rest_menu", oPC, 1, "");

            Scheduler.assign(oPC, () -> takeGoldFromCreature(getGold(oPC), oPC, true));

            NWObject knife = createItemOnObject("survival_knife", oPC, 1, "");
            setName(knife, getName(oPC, false) + "'s Survival Knife");
            setItemCursedFlag(knife, true);
            DurabilitySystem.SetItemMaxDurability(knife, 5);
            DurabilitySystem.SetItemDurability(knife, 5);

            NWObject hammer = createItemOnObject("basic_hammer", oPC, 1, "");
            setName(hammer, getName(oPC, false) + "'s Hammer");
            setItemCursedFlag(hammer, true);
            DurabilitySystem.SetItemMaxDurability(hammer, 5);
            DurabilitySystem.SetItemDurability(hammer, 5);

            NWObject darts = createItemOnObject("nw_wthdt001", oPC, 50, ""); // 50x Dart
            setName(darts, "Starting Darts");
            setItemCursedFlag(darts, true);

            int numberOfFeats = NWNX_Creature.GetFeatCount(oPC);
            for(int currentFeat = numberOfFeats; currentFeat >= 0; currentFeat--)
            {
                NWNX_Creature.RemoveFeat(oPC, NWNX_Creature.GetFeatByIndex(oPC, currentFeat-1));
            }

            NWNX_Creature.SetClassByPosition(oPC, 0, Class.TYPE_FIGHTER);
            NWNX_Creature.AddFeatByLevel(oPC, Feat.ARMOR_PROFICIENCY_LIGHT, 1);
            NWNX_Creature.AddFeatByLevel(oPC, Feat.ARMOR_PROFICIENCY_MEDIUM, 1);
            NWNX_Creature.AddFeatByLevel(oPC, Feat.ARMOR_PROFICIENCY_HEAVY, 1);
            NWNX_Creature.AddFeatByLevel(oPC, Feat.SHIELD_PROFICIENCY, 1);
            NWNX_Creature.AddFeatByLevel(oPC, Feat.WEAPON_PROFICIENCY_EXOTIC, 1);
            NWNX_Creature.AddFeatByLevel(oPC, Feat.WEAPON_PROFICIENCY_MARTIAL, 1);
            NWNX_Creature.AddFeatByLevel(oPC, Feat.WEAPON_PROFICIENCY_SIMPLE, 1);

            for(int iCurSkill = 1; iCurSkill <= 27; iCurSkill++)
            {
                NWNX_Creature.SetSkillRank(oPC, iCurSkill-1, 0);
            }
            setFortitudeSavingThrow(oPC, 0);
            setReflexSavingThrow(oPC,  0);
            setWillSavingThrow(oPC, 0);

            int classID = getClassByPosition(1, oPC);

            for(int index = 0; index <= 255; index++)
            {
                NWNX_Creature.RemoveKnownSpell(oPC, classID, 0, index);
            }

            PlayerRepository repo = new PlayerRepository();
            PlayerEntity entity = pcGO.createEntity();
            repo.save(entity);

            SkillSystem.ApplyStatChanges(oPC, null);
            Scheduler.delay(oPC, 1000, () -> applyEffectToObject(DurationType.INSTANT, effectHeal(999), oPC, 0.0f));

            InitializeHotBar(oPC);
        }
    }

    public static boolean IsPCInitialized(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oDatabase = pcGO.GetDatabaseItem();
        boolean missingStringID = getLocalString(oDatabase, Constants.PCIDNumberVariable).equals("");
        return !missingStringID && getIsObjectValid(oDatabase);
    }

    private static void InitializeHotBar(NWObject oPC)
    {
        QuickBarSlot slot = NWNX_Player_QuickBarSlot.UseItem(getItemPossessedBy(oPC, "open_rest_menu"), 0);
        NWNX_Player.SetQuickBarSlot(oPC, 0, slot);

        slot = NWNX_Player_QuickBarSlot.UseItem(getItemPossessedBy(oPC, "database"), 0);
        NWNX_Player.SetQuickBarSlot(oPC, 1, slot);

        slot = NWNX_Player_QuickBarSlot.EquipItem(getItemPossessedBy(oPC, "survival_knife"), NWObject.INVALID);
        NWNX_Player.SetQuickBarSlot(oPC, 2, slot);

        slot = NWNX_Player_QuickBarSlot.EquipItem(getItemPossessedBy(oPC, "basic_hammer"), NWObject.INVALID);
        NWNX_Player.SetQuickBarSlot(oPC, 3, slot);

        slot = NWNX_Player_QuickBarSlot.EquipItem(getItemPossessedBy(oPC, "nw_wthdt001"), NWObject.INVALID);
        NWNX_Player.SetQuickBarSlot(oPC, 4, slot);


        QuickBarSlot empty = NWNX_Player_QuickBarSlot.Empty(QuickBarSlotType.EMPTY);
        NWNX_Player.SetQuickBarSlot(oPC, 5, empty);
        NWNX_Player.SetQuickBarSlot(oPC, 6, empty);
        NWNX_Player.SetQuickBarSlot(oPC, 7, empty);
        NWNX_Player.SetQuickBarSlot(oPC, 8, empty);
        NWNX_Player.SetQuickBarSlot(oPC, 9, empty);
        NWNX_Player.SetQuickBarSlot(oPC, 10, empty);
        NWNX_Player.SetQuickBarSlot(oPC, 11, empty);
    }
}
