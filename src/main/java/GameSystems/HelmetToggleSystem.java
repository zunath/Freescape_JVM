package GameSystems;

import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class HelmetToggleSystem {

    public static void ToggleHelmetDisplay(NWObject oPC)
    {
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerEntity entity = playerRepo.GetByPlayerID(pcGO.getUUID());

        if(entity == null) return;

        entity.setDisplayHelmet(!entity.isDisplayHelmet());
        playerRepo.save(entity);

        if(entity.isDisplayHelmet())
        {
            floatingTextStringOnCreature("Now showing equipped helmet.", oPC, false);
        }
        else
        {
            floatingTextStringOnCreature("Now hiding equipped helmet.", oPC, false);
        }

        NWObject helmet = getItemInSlot(InventorySlot.HEAD, oPC);
        if(getIsObjectValid(helmet))
        {
            setHiddenWhenEquipped(helmet, !entity.isDisplayHelmet());
        }
    }

    public static void OnModuleItemEquipped()
    {
        NWObject oPC = getPCItemLastEquippedBy();
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        NWObject item = getPCItemLastEquipped();
        if(getBaseItemType(item) != BaseItem.HELMET) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerEntity entity = playerRepo.GetByPlayerID(pcGO.getUUID());

        setHiddenWhenEquipped(item, !entity.isDisplayHelmet());
    }

    public static void OnModuleItemUnequipped()
    {
        NWObject oPC = getPCItemLastUnequippedBy();
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        NWObject item = getPCItemLastUnequipped();
        if(getBaseItemType(item) != BaseItem.HELMET) return;

        setHiddenWhenEquipped(item, false);
    }

}
