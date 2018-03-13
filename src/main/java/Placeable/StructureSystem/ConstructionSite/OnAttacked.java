package Placeable.StructureSystem.ConstructionSite;


import Common.IScriptEventHandler;
import Data.Repository.StructureRepository;
import Entities.ConstructionSiteComponentEntity;
import Entities.ConstructionSiteEntity;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameObject.PlayerGO;
import GameSystems.DurabilitySystem;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import GameSystems.StructureSystem;
import Helper.ColorToken;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.DurationType;
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
        int mangleChance = CalculateMangleChance(oPC, entity.getBlueprint().getLevel(), rank);
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
                    int reuseChance = isMangle ? 0 : PerkSystem.GetPCPerkLevel(oPC, PerkID.ConservativeConstruction) * 2;
                    if(ThreadLocalRandom.current().nextInt(100) + 1 <= reuseChance)
                    {
                        NWScript.sendMessageToPC(oPC, "You conserve a resource...");
                    }
                    else
                    {
                        ItemHelper.ReduceItemStack(item);
                    }

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

            // Speedy Builder - Grants haste for 8 seconds
            int perkChance = PerkSystem.GetPCPerkLevel(oPC, PerkID.SpeedyBuilder) * 10;
            if(ThreadLocalRandom.current().nextInt(100) + 1 <= perkChance)
            {
                NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectHaste(), oPC, 8.0f);
            }
        }
        else
        {
            Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));
        }
    }

    private static int CalculateMangleChance(NWObject oPC, int level, int rank)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.MangleMaster);
        int delta = level - rank;
        int perkReduction = perkLevel * 5;
        int mangleChance;
        if(delta <= 3) mangleChance = 0;
        else if(delta <= 4) mangleChance = 15;
        else if(delta <= 5) mangleChance = 25;
        else if(delta <= 6) mangleChance = 50;
        else mangleChance = 95;

        return mangleChance - perkReduction;
    }
}
