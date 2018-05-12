package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import Enumerations.PerkID;
import GameSystems.FarmingSystem;
import GameSystems.PerkSystem;
import GameSystems.SpawnSystem;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject point) {
        NWObject oPC = getLastDisturbed();
        NWObject oItem = getInventoryDisturbItem();
        int disturbType = getInventoryDisturbType();

        if(disturbType == InventoryDisturbType.ADDED)
        {
            ItemHelper.ReturnItem(oPC, oItem);
        }
        else
        {
            NWObject[] items = getItemsInInventory(point);
            if(items.length <= 0 && getLocalInt(point, "FORAGE_POINT_FULLY_HARVESTED") == 1)
            {
                String seed = getLocalString(point, "FORAGE_POINT_SEED");
                if(!seed.equals(""))
                {
                    createObject(ObjectType.ITEM, seed, getLocation(point), false, "");

                    int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.SeedPicker);
                    if(ThreadLocalRandom.current().nextInt(100) + 1 <= perkLevel*10)
                    {
                        createObject(ObjectType.ITEM, seed, getLocation(point), false, "");
                    }
                }

                SpawnSystem.AddToRespawnQueue(point);
                destroyObject(point, 0.0f);
                FarmingSystem.RemoveGrowingPlant(point);
            }
        }
    }
}
