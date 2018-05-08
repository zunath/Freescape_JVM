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

        switch(backgroundID)
        {
            case BackgroundID.Weaponsmith:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.MetalWeaponBlueprints);
                break;
            case BackgroundID.Armorsmith:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.LightArmorBlueprints);
                break;
            case BackgroundID.Knight:
                NWObject armor = createItemOnObject("bkg_knightarmor", oPC, 1, "");
                setItemCursedFlag(armor, true);
                setName(armor, pcName + "'s Knight Armor");
                break;
            case BackgroundID.Wizard:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.FireBlast);
                break;
            case BackgroundID.Cleric:
                PerkSystem.DoPerkUpgrade(oPC, PerkID.Recover);
                break;
            case BackgroundID.Archer:
                NWObject bow = createItemOnObject("bkg_archerbow", oPC, 1, "");
                setItemCursedFlag(bow, true);
                setName(bow, pcName + "'s Archer Bow");

                NWObject arrows = createItemOnObject("nw_wamar001", oPC, 99, "");
                setItemCursedFlag(arrows, true);
                setName(arrows, pcName + "'s Archer Arrows");
                break;
            case BackgroundID.Crossbowman:
                NWObject crossbow = createItemOnObject("bkg_cbmcrbow", oPC, 1, "");
                setItemCursedFlag(crossbow, true);
                setName(crossbow, pcName + "'s Crossbowman Crossbow");

                NWObject bolts = createItemOnObject("nw_wambo001", oPC, 99, "");
                setItemCursedFlag(bolts, true);
                setName(bolts, pcName + "'s Crossbowman Bolts");
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

        }

        SkillSystem.ApplyStatChanges(oPC, null);
    }


}
