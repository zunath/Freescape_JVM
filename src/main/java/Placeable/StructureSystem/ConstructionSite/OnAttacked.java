package Placeable.StructureSystem.ConstructionSite;


import Entities.ConstructionSiteComponentEntity;
import Entities.ConstructionSiteEntity;
import Common.IScriptEventHandler;
import Data.Repository.StructureRepository;
import Enumerations.SkillID;
import GameSystems.DurabilitySystem;
import GameSystems.SkillSystem;
import GameSystems.StructureSystem;
import Helper.ColorToken;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import java.util.concurrent.ThreadLocalRandom;

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
        int rank = SkillSystem.GetPCSkill(oPC, SkillID.Construction).getRank();
        int mangleChance = CalculateMangleChance(entity.getBlueprint().getLevel(), rank);
        boolean isMangle = ThreadLocalRandom.current().nextInt(100)+1 <= mangleChance;
        boolean foundResource = false;
        String updateMessage = "You lack the necessary resources...";

        int totalAmount = 0;
        for(ConstructionSiteComponentEntity comp: entity.getComponents())
        {
            if(comp.getQuantity() > 0 && !foundResource)
            {
                NWObject item = NWScript.getItemPossessedBy(oPC, comp.getStructureComponent().getResref());
                if(NWScript.getIsObjectValid(item))
                {
                    ItemHelper.ReduceItemStack(item);
                    if(isMangle)
                    {
                        NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You mangle a resource due to your lack of skill..." + ColorToken.End());
                        return;
                    }

                    String name = ItemHelper.GetNameByResref(comp.getStructureComponent().getResref());
                    comp.setQuantity(comp.getQuantity() - 1);
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

            int xp = (int)SkillSystem.CalculateSkillAdjustedXP(100, 0, rank);
            SkillSystem.GiveSkillXP(oPC, SkillID.Construction, xp);
        }
        else
        {
            Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));
        }
    }

    private static int CalculateMangleChance(int level, int rank)
    {
        int delta = level - rank;
        if(delta <= 3) return 0;

        if(delta <= 4) return 20;
        else if(delta <= 5) return 35;
        else if(delta <= 6) return 50;
        else return 95;
    }
}
