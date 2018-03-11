package Placeable.StructureSystem.ConstructionSite;


import Entities.ConstructionSiteComponentEntity;
import Entities.ConstructionSiteEntity;
import Common.IScriptEventHandler;
import Data.Repository.StructureRepository;
import GameSystems.DurabilitySystem;
import GameSystems.StructureSystem;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

@SuppressWarnings("unused")
public class OnAttacked implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oSite) {

        final NWObject oPC = NWScript.getLastAttacker(oSite);
        int constructionSiteID = StructureSystem.GetConstructionSiteID(oSite);

        if(constructionSiteID <= 0)
        {
            NWScript.floatingTextStringOnCreature("You must select a blueprint before you can build.", oPC, false);
            Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));
            return;
        }

        NWObject weapon = NWScript.getLastWeaponUsed(oPC);
        int weaponType = NWScript.getBaseItemType(weapon);

        if(weaponType != BaseItem.LIGHTHAMMER)
        {
            NWScript.floatingTextStringOnCreature("A hammer must be equipped to build this structure.", oPC, false);
            Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));
            return;
        }

        // Offhand weapons don't contribute to building.
        if(NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC).equals(weapon))
        {
            return;
        }

        if(!StructureSystem.IsConstructionSiteValid(oSite))
        {
            NWScript.floatingTextStringOnCreature("Construction site is invalid. Please click the construction site to find out more.", oPC, false);
            Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));
            return;
        }

        StructureRepository repo = new StructureRepository();
        ConstructionSiteEntity entity = repo.GetConstructionSiteByID(constructionSiteID);

        boolean foundResource = false;
        String updateMessage = "You lack the necessary resources...\n\n";

        int totalAmount = 0;
        for(ConstructionSiteComponentEntity comp: entity.getComponents())
        {
            if(comp.getQuantity() > 0 && !foundResource)
            {
                NWObject item = NWScript.getItemPossessedBy(oPC, comp.getStructureComponent().getResref());
                if(NWScript.getIsObjectValid(item))
                {
                    String name = ItemHelper.GetNameByResref(comp.getStructureComponent().getResref());
                    comp.setQuantity(comp.getQuantity() - 1);
                    NWScript.destroyObject(item, 0.0f);
                    updateMessage = "You need " + comp.getQuantity() + " " + name + " to complete this project.";
                    foundResource = true;
                }
            }
            totalAmount += comp.getQuantity();
        }

        final String messageCopy = updateMessage;
        Scheduler.delay(oPC, 750, () -> NWScript.sendMessageToPC(oPC, messageCopy));

        if(totalAmount <= 0)
        {
            StructureSystem.CompleteStructure(oSite);
        }
        else if(foundResource)
        {
            repo.Save(entity);
            DurabilitySystem.RunItemDecay(oPC, weapon);
        }
        else
        {
            Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));
        }
    }
}
