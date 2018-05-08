package GameSystems;

import Data.Repository.BackgroundRepository;
import Data.Repository.PlayerRepository;
import Entities.BackgroundEntity;
import Entities.PlayerEntity;
import Enumerations.BackgroundID;
import Enumerations.PerkID;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.List;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class BackgroundSystem {

    public static void OnModuleEnter()
    {
        PlayerRepository repo = new PlayerRepository();
        NWObject oPC = getEnteringObject();
        if(!getIsPC(oPC) || getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject token = getItemPossessedBy(oPC, "bkg_token");
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());

        if(entity.getBackgroundID() > 0) return;

        if(!getIsObjectValid(token))
        {
            createItemOnObject("bkg_token", oPC, 1, "");
        }
    }

    public static List<BackgroundEntity> GetActiveBackgrounds()
    {
        BackgroundRepository repo = new BackgroundRepository();
        return repo.GetActiveBackgrounds();
    }

    public static void SelectBackground(NWObject oPC, BackgroundEntity entity)
    {
        PlayerRepository repo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerEntity pcEntity = repo.GetByPlayerID(pcGO.getUUID());
        pcEntity.setBackgroundID(entity.getBackgroundID());
        repo.save(pcEntity);
        ApplyBackgroundBonuses(oPC, entity.getBackgroundID());

        NWObject token = getItemPossessedBy(oPC, "bkg_token");
        destroyObject(token, 0.0f);

        floatingTextStringOnCreature("Background confirmed!", oPC, false);
    }

    private static void ApplyBackgroundBonuses(NWObject oPC, int backgroundID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        String pcName = getName(oPC, false);
        PlayerEntity pcEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        String item1Resref = "";
        int item1Quantity = 1;
        String item2Resref = "";
        int item2Quantity = 1;

        switch(backgroundID)
        {
            case BackgroundID.Weaponsmith:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.MetalWeaponBlueprints);
                break;
            case BackgroundID.Armorsmith:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.LightArmorBlueprints);
                break;
            case BackgroundID.Knight:
                item1Resref = "bkg_knightarmor";
                break;
            case BackgroundID.Wizard:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.FireBlast);
                break;
            case BackgroundID.Cleric:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.Recover);
                break;
            case BackgroundID.Archer:
                item1Resref = "bkg_archerbow";
                item2Resref = "nw_wamar001";
                item2Quantity = 99;
                break;
            case BackgroundID.Crossbowman:
                item1Resref = "bkg_cbmcrbow";
                item2Resref = "nw_wambo001";
                item2Quantity = 99;
                break;
            case BackgroundID.Chef:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.FoodRecipes);
                break;
            case BackgroundID.Metalworker:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.MetalworkingComponentBlueprints);
                break;
            case BackgroundID.Woodworker:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.WoodComponentsBlueprints);
                break;
            case BackgroundID.Vagabond:
                pcEntity.setUnallocatedSP(pcEntity.getUnallocatedSP()+3);
                playerRepo.save(pcEntity);
                break;
            case BackgroundID.Guard:
                item1Resref = "bkg_guardlgswd";
                item2Resref = "bkg_guardshld";
                break;
            case BackgroundID.Berserker:
                item1Resref = "bkg_bersgswd";
                break;
            case BackgroundID.TwinBladeSpecialist:
                item1Resref = "bkg_spectwinbld";
                break;
            case BackgroundID.MartialArtist:
                item1Resref = "bkg_mrtlglove";
                item2Resref = "bkg_mtlshuriken";
                item2Quantity = 50;
                break;
            case BackgroundID.Miner:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.Miner);
                PerkSystem.DoPerkUpgrade(oPC, PerkID.Miner);
                break;
            case BackgroundID.Lumberjack:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.Lumberjack);
                PerkSystem.DoPerkUpgrade(oPC, PerkID.Lumberjack);
                break;
            case BackgroundID.ConstructionBuilder:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.VanityBlueprints);
                break;
            case BackgroundID.Medic:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.HealingKitExpert);
                PerkSystem.DoPerkUpgrade(oPC, PerkID.ImmediateImprovement);
                break;
            case BackgroundID.Farmer:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.ExpertFarmer);
                break;
        }

        if(!item1Resref.equals(""))
        {
            NWObject oItem1 = createItemOnObject(item1Resref, oPC, item1Quantity, "");
            setItemCursedFlag(oItem1, true);
            setName(oItem1, pcName + "'s " + getName(oItem1, false));
        }
        if(!item2Resref.equals(""))
        {
            NWObject oItem2 = createItemOnObject(item2Resref, oPC, item2Quantity, "");
            setItemCursedFlag(oItem2, true);
            setName(oItem2, pcName + "'s " + getName(oItem2, false));
        }

        SkillSystem.ApplyStatChanges(oPC, null);
    }


}
