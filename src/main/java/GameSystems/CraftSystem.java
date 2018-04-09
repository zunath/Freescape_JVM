package GameSystems;

import Data.Repository.CraftRepository;
import Data.Repository.SkillRepository;
import Entities.CraftBlueprintEntity;
import Entities.CraftComponentEntity;
import Entities.PCSkillEntity;
import Enumerations.CraftDeviceID;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ErrorHelper;
import Helper.ItemHelper;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;
import org.nwnx.nwnx2.jvm.constants.VfxComBlood;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;


public class CraftSystem {

    private static final float BaseCraftDelay = 18.0f;


    public static void CraftItem(final NWObject oPC, final NWObject device, final int blueprintID)
    {
        final PlayerGO pcGO = new PlayerGO(oPC);
        CraftRepository repo = new CraftRepository();
        CraftBlueprintEntity blueprint = repo.GetBlueprintByID(blueprintID);
        if(blueprint == null) return;
        boolean requiresTools = false;
        boolean foundTools = false;

        if(pcGO.isBusy())
        {
            sendMessageToPC(oPC, "You are too busy right now.");
            return;
        }

        // Check for tools, if necessary.
        if(blueprint.getCraftTierLevel() > 0)
        {
            requiresTools = true;
            NWObject tools = getLocalObject(device, "CRAFT_DEVICE_TOOLS");
            if(getIsObjectValid(tools))
            {
                foundTools = true;
            }
        }

        if(requiresTools != foundTools)
        {
            sendMessageToPC(oPC, ColorToken.Red() + "Tools were not found. Please place the tools you wish to use inside the crafting device." + ColorToken.End());
            pcGO.setIsBusy(false);
            return;
        }
        pcGO.setIsBusy(true);

        boolean allComponentsFound = CheckItemCounts(oPC, device, blueprint.getComponents());

        if(allComponentsFound)
        {
            final float modifiedCraftDelay = CalculateCraftingDelay(oPC, blueprint.getSkill().getSkillID());

            applyEffectToObject(DurationType.TEMPORARY, effectCutsceneImmobilize(), oPC, modifiedCraftDelay + 0.1f);
            Scheduler.assign(oPC, () -> {
                clearAllActions(false);
                actionPlayAnimation(Animation.LOOPING_GET_MID, 1.0f, modifiedCraftDelay);
            });
            Scheduler.delay(device, 1000 * floatToInt(modifiedCraftDelay / 2.0f), () -> applyEffectToObject(DurationType.INSTANT, effectVisualEffect(VfxComBlood.SPARK_MEDIUM, false), device, 0.0f));

            NWNX_Player.StartGuiTimingBar(oPC, floatToInt(modifiedCraftDelay), "");

            Scheduler.delay(oPC, floatToInt(modifiedCraftDelay * 1000), () -> {
                try
                {
                    RunCreateItem(oPC, device, blueprintID);
                    pcGO.setIsBusy(false);
                }
                catch (Exception ex)
                {
                    ErrorHelper.HandleException(ex, "");
                }
            });
        }
        else
        {
            sendMessageToPC(oPC, ColorToken.Red() + "You are missing required components..." + ColorToken.End());
        }
    }

    private static float CalculateCraftingDelay(NWObject oPC, int skillID)
    {
        int perkID;
        float adjustedSpeed = 1.0f;

        if(skillID == SkillID.Metalworking) perkID = PerkID.SpeedyMetalworking;
        else if(skillID == SkillID.Weaponsmith) perkID = PerkID.SpeedyWeaponsmith;
        else if(skillID == SkillID.Armorsmith) perkID = PerkID.SpeedyArmorsmith;
        else if(skillID == SkillID.Cooking) perkID = PerkID.SpeedyCooking;
        else if(skillID == SkillID.Woodworking) perkID = PerkID.SpeedyWoodworking;
        else return BaseCraftDelay;

        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, perkID);

        switch (perkLevel)
        {
            case 1: adjustedSpeed = 0.9f; break;
            case 2: adjustedSpeed = 0.8f; break;
            case 3: adjustedSpeed = 0.7f; break;
            case 4: adjustedSpeed = 0.6f; break;
            case 5: adjustedSpeed = 0.5f; break;
            case 6: adjustedSpeed = 0.4f; break;
            case 7: adjustedSpeed = 0.3f; break;
            case 8: adjustedSpeed = 0.2f; break;
            case 9: adjustedSpeed = 0.1f; break;
            case 10: adjustedSpeed = 0.01f; break;
        }

        return BaseCraftDelay * adjustedSpeed;
    }

    private static boolean CheckItemCounts(NWObject oPC, NWObject device, List<CraftComponentEntity> componentList)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        boolean allComponentsFound = false;
        HashMap<String, Integer> components = new HashMap<>();

        for(CraftComponentEntity component : componentList)
        {
            components.put(component.getItemResref(), component.getQuantity());
        }

        NWObject tempStorage = createObject(ObjectType.PLACEABLE, "craft_temp_store", getLocation(device), false, "");
        setLocalObject(device, "CRAFT_TEMP_STORAGE", tempStorage);

        for(NWObject item : getItemsInInventory(device)) {
            String resref = getResRef(item);
            if (components.containsKey(resref) && components.get(resref) > 0) {
                components.put(resref, components.get(resref) - 1);

                copyItem(item, tempStorage, true);
                destroyObject(item, 0.0f);
            }

            int remainingQuantities = 0;
            for (int quantity : components.values())
                remainingQuantities += quantity;

            if (remainingQuantities <= 0) {
                allComponentsFound = true;
                break;
            }
        }

        if(!allComponentsFound)
        {
            for(NWObject item : getItemsInInventory(tempStorage))
            {
                copyItem(item, device, true);
                destroyObject(item, 0.0f);
            }
            pcGO.setIsBusy(false);
        }

        return allComponentsFound;
    }

    private static void RunCreateItem(NWObject oPC, NWObject device, int blueprintID)
    {
        NWObject tempStorage = getLocalObject(device, "CRAFT_TEMP_STORAGE");
        NWObject tools = getLocalObject(device, "CRAFT_DEVICE_TOOLS");
        PlayerGO pcGO = new PlayerGO(oPC);
        CraftRepository craftRepo = new CraftRepository();
        SkillRepository skillRepo = new SkillRepository();

        CraftBlueprintEntity blueprint = craftRepo.GetBlueprintByID(blueprintID);
        PCSkillEntity pcSkill = skillRepo.GetPCSkillByID(pcGO.getUUID(), blueprint.getSkill().getSkillID());

        int pcEffectiveLevel = CalculatePCEffectiveLevel(device, pcSkill.getRank());

        float chance = CalculateChanceToCreateItem(pcEffectiveLevel, blueprint.getLevel());
        float roll = ThreadLocalRandom.current().nextFloat() * 100.0f;
        float xpModifier;

        if(roll <= chance)
        {
            // Success!
            for(NWObject item : getItemsInInventory(tempStorage))
            {
                destroyObject(item, 0.0f);
            }

            NWObject craftedItem = createItemOnObject(blueprint.getItemResref(), oPC, blueprint.getQuantity(), "");
            setIdentified(craftedItem, true);

            // If item isn't stackable, loop through and create as many as necessary.
            if(getItemStackSize(craftedItem) < blueprint.getQuantity())
            {
                for(int x = 2; x <= blueprint.getQuantity(); x++)
                {
                    craftedItem = createItemOnObject(blueprint.getItemResref(), oPC, 1, "");
                    setIdentified(craftedItem, true);
                }
            }

            sendMessageToPC(oPC, "You created " + blueprint.getQuantity() + "x " + blueprint.getItemName() + "!");
            xpModifier = 1.0f;
        }
        else
        {
            // Failure...
            NWObject[] items = getItemsInInventory(tempStorage);
            int chanceToLoseItems = 20;

            for(NWObject item : items)
            {
                if(ThreadLocalRandom.current().nextInt(100) > chanceToLoseItems)
                {
                    copyItem(item, device, true);
                }
                destroyObject(item, 0.0f);
            }

            sendMessageToPC(oPC, "You failed to create that item...");
            xpModifier = 0.2f;
        }

        float xp = SkillSystem.CalculateSkillAdjustedXP(250, blueprint.getLevel(), pcSkill.getRank()) * xpModifier;
        destroyObject(tempStorage, 0.0f);
        SkillSystem.GiveSkillXP(oPC, blueprint.getSkill().getSkillID(), (int)xp);

        if(getIsObjectValid(tools))
        {
            float min = 0.05f;
            float max = 0.1f;
            float reduceDurability = min + ThreadLocalRandom.current().nextFloat() * (max - min);
            DurabilitySystem.RunItemDecay(oPC, tools, reduceDurability);
        }

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= 3)
        {
            FoodSystem.DecreaseHungerLevel(oPC, 1);
        }
    }

    private static String CalculateDifficulty(int pcLevel, int blueprintLevel)
    {
        int delta = pcLevel - blueprintLevel;
        String difficulty = "";

        if(delta <= -5)
        {
            difficulty = ColorToken.Custom(255, 62, 150) + "Impossible" + ColorToken.End();
        }
        else if(delta >= 4)
        {
            difficulty = ColorToken.Custom(102, 255, 102) + "Trivial" + ColorToken.End();
        }
        else
        {
            switch (delta)
            {
                case -4:
                    difficulty = ColorToken.Custom(220, 20, 60) + "Extremely Difficult" + ColorToken.End();
                    break;
                case -3:
                    difficulty = ColorToken.Custom(255, 69, 0) + "Very Difficult" + ColorToken.End();
                    break;
                case -2:
                    difficulty = ColorToken.Custom(255, 165, 0) + "Difficult" + ColorToken.End();
                    break;
                case -1:
                    difficulty = ColorToken.Custom(238, 238, 0) + "Challenging" + ColorToken.End();
                    break;
                case 0:
                    difficulty = ColorToken.Custom(255, 255, 255) + "Moderate" + ColorToken.End();
                    break;
                case 1:
                    difficulty = ColorToken.Custom(65, 105, 225) + "Easy" + ColorToken.End();
                    break;
                case 2:
                    difficulty = ColorToken.Custom(113, 113, 198) + "Very Easy" + ColorToken.End();
                    break;
                case 3:
                    difficulty = ColorToken.Custom(153, 255, 255) + "Extremely Easy" + ColorToken.End();
                    break;
            }
        }


        return difficulty;
    }


    private static float CalculateChanceToCreateItem(int pcLevel, int blueprintLevel)
    {
        int delta = pcLevel - blueprintLevel;
        float percentage = 0.0f;

        if (delta <= -5)
        {
            percentage = 0.0f;
        }
        else if(delta >= 4)
        {
            percentage = 95.0f;
        }
        else
        {
            switch (delta)
            {
                case -4:
                    percentage = 15.0f;
                    break;
                case -3:
                    percentage = 25.0f;
                    break;
                case -2:
                    percentage = 40.0f;
                    break;
                case -1:
                    percentage = 60.0f;
                    break;
                case 0:
                    percentage = 75.0f;
                    break;
                case 1:
                    percentage = 82.5f;
                    break;
                case 2:
                    percentage = 87.0f;
                    break;
                case 3:
                    percentage = 90.0f;
                    break;
            }
        }


        return percentage;
    }

    private static int CalculatePCEffectiveLevel(NWObject device, int skillRank)
    {
        int deviceID = getLocalInt(device, "CRAFT_DEVICE_ID");
        NWObject tools = getLocalObject(device, "CRAFT_DEVICE_TOOLS");
        int effectiveLevel = skillRank;

        if(getIsObjectValid(tools))
        {
            ItemGO toolsGO = new ItemGO(tools);
            int toolBonus = 0;

            switch (deviceID)
            {
                case CraftDeviceID.ArmorsmithBench: toolBonus = toolsGO.getCraftBonusArmorsmith(); break;
                case CraftDeviceID.Cookpot: toolBonus = toolsGO.getCraftBonusCooking(); break;
                case CraftDeviceID.MetalworkingBench: toolBonus = toolsGO.getCraftBonusMetalworking(); break;
                case CraftDeviceID.WeaponsmithBench: toolBonus = toolsGO.getCraftBonusWeaponsmith(); break;
                case CraftDeviceID.WoorkworkingBench: toolBonus = toolsGO.getCraftBonusWoodworking(); break;
            }

            effectiveLevel += toolBonus;
        }

        return effectiveLevel;
    }

    public static String BuildBlueprintHeader(NWObject oPC, int blueprintID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        CraftRepository craftRepo = new CraftRepository();
        SkillRepository skillRepo = new SkillRepository();

        CraftBlueprintEntity blueprint = craftRepo.GetBlueprintByID(blueprintID);
        PCSkillEntity pcSkill = skillRepo.GetPCSkillByID(pcGO.getUUID(), blueprint.getSkill().getSkillID());


        String header = ColorToken.Green() + "Blueprint: " + ColorToken.End() + ColorToken.White() + blueprint.getItemName() + ColorToken.End() + "\n\n";
        header += ColorToken.Green() + "Skill: " + ColorToken.End() + ColorToken.White() + pcSkill.getSkill().getName() + " (" + pcSkill.getRank() + ")" + ColorToken.End() + "\n";

        if(blueprint.getCraftTierLevel() > 0)
        {
            header += ColorToken.Green() + "Required Tool Level: " + ColorToken.End() + blueprint.getCraftTierLevel() + "\n";
        }

        header += ColorToken.Green() + "Difficulty: " + ColorToken.End() + CalculateDifficulty(pcSkill.getRank(), blueprint.getLevel()) + "\n\n";
        header += ColorToken.Green() + "Components: " + ColorToken.End() + "\n\n";

        for(CraftComponentEntity component : blueprint.getComponents())
        {
            String name = ItemHelper.GetNameByResref(component.getItemResref());
            header += ColorToken.White() + component.getQuantity() + "x " + name + ColorToken.End() + "\n";
        }

        return header;
    }

}
