package Placeable.CraftingForge;

import Common.IScriptEventHandler;
import Entities.PCSkillEntity;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameObject.PlayerGO;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.AnimationLooping;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.Arrays;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject forge) {
        if(getInventoryDisturbType() != InventoryDisturbType.ADDED) return;

        NWObject pc = getLastDisturbed();
        NWObject item = getInventoryDisturbItem();

        if(!checkValidity(forge, pc, item)) return;
        startSmelt(forge, pc, item);
    }

    private boolean checkValidity(NWObject forge, NWObject pc, NWObject item)
    {
        PlayerGO pcGO = new PlayerGO(pc);
        String resref = getResRef(item);

        if(pcGO.isBusy())
        {
            returnItemToPC(pc, item, "You are too busy.");
            return false;
        }

        if(getIsObjectValid(getLocalObject(forge, "FORGE_USER")))
        {
            returnItemToPC(pc, item, "This forge is currently in use. Please wait...");
            return false;
        }

        String[] allowed = {
                "coal", "copper_ore", "tin_ore", "iron_ore", "gold_ore", "platinum_ore", "adamantium_ore",
                "cobalt_ore", "silver_ore", "titanium_ore", "mithril_ore"
        };

        if(!Arrays.asList(allowed).contains(resref))
        {
            returnItemToPC(pc, item, "Only coal and ore may be placed inside.");
            return false;
        }

        int level = CraftingForgeCommon.GetIngotLevel(resref);
        PCSkillEntity pcSkill = SkillSystem.GetPCSkill(pc, SkillID.Metalworking);
        if(pcSkill == null) return false;

        int delta = pcSkill.getRank() - level;
        if(delta <= -4)
        {
            returnItemToPC(pc, item, "You do not have enough skill to smelt this ore.");
            return false;
        }

        int pcPerklevel = PerkSystem.GetPCPerkLevel(pc, PerkID.Smelting);
        int orePerkLevel = CraftingForgeCommon.GetIngotPerkLevel(resref);

        if(pcPerklevel < orePerkLevel)
        {
            returnItemToPC(pc, item, "You do not have the perk necessary to smelt this ore.");
            return false;
        }

        return true;
    }

    private void startSmelt(NWObject forge, NWObject pc, NWObject item)
    {
        PlayerGO pcGO = new PlayerGO(pc);
        String resref = getResRef(item);

        int charges = getLocalInt(forge, "FORGE_CHARGES");
        if(resref.equals("coal"))
        {
            destroyObject(item, 0.0f);
            charges += 10 + calculatePerkCoalBonusCharges(pc);
            setLocalInt(forge, "FORGE_CHARGES", charges);

            NWObject flames = getLocalObject(forge, "FORGE_FLAMES");
            if(!getIsObjectValid(flames))
            {
                NWVector flamePosition = Bioware.Position.GetChangedPosition(getPosition(forge), 0.36f, getFacing(forge));
                NWLocation flameLocation = location(getArea(forge), flamePosition, 0.0f);
                flames = createObject(ObjectType.PLACEABLE, "forge_flame", flameLocation, false, "");
                setLocalObject(forge, "FORGE_FLAMES", flames);
            }

            return;
        }
        else if(charges <= 0)
        {
            returnItemToPC(pc, item, "You must light the forge with coal before smelting.");
            return;
        }
        destroyObject(item, 0.0f);

        // Ready to smelt
        float baseCraftDelay = 18.0f - (PerkSystem.GetPCPerkLevel(pc, PerkID.SpeedySmelter) * 0.1f);

        pcGO.setIsBusy(true);
        setLocalObject(forge, "FORGE_USER", pc);
        setLocalObject(pc, "FORGE", forge);
        setLocalString(forge, "FORGE_ORE", resref);

        NWNX_Player.StartGuiTimingBar(pc, baseCraftDelay, "Placeable.CraftingForge.FinishSmelt");

        applyEffectToObject(DurationType.TEMPORARY, effectCutsceneImmobilize(), pc, baseCraftDelay);
        Scheduler.assign(pc, () -> actionPlayAnimation(AnimationLooping.GET_MID, 1.0f, baseCraftDelay));

    }

    private void returnItemToPC(NWObject pc, NWObject item, String message)
    {
        copyItem(item, pc, true);
        destroyObject(item, 0.0f);
        sendMessageToPC(pc, message);
    }

    private int calculatePerkCoalBonusCharges(NWObject pc)
    {
        int perkLevel = PerkSystem.GetPCPerkLevel(pc, PerkID.CoalManagement);

        switch (perkLevel)
        {
            case 1: return 2;
            case 2: return 3;
            case 3: return 4;
            case 4: return 5;
            case 5: return 8;
            case 6: return 10;
            default: return 0;
        }
    }
}
