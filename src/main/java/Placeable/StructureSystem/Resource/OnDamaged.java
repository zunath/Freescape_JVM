package Placeable.StructureSystem.Resource;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import GameSystems.DurabilitySystem;
import GameSystems.MagicSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class OnDamaged implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastDamager(objSelf);
        NWObject oWeapon = NWScript.getLastWeaponUsed(oPC);
        NWLocation location = NWScript.getLocation(oPC);
        String resourceItemResref = NWScript.getLocalString(objSelf, "RESOURCE_RESREF");
        int activityID = NWScript.getLocalInt(objSelf, "RESOURCE_ACTIVITY");
        String resourceName = NWScript.getLocalString(objSelf, "RESOURCE_NAME");
        int resourceCount = NWScript.getLocalInt(objSelf, "RESOURCE_COUNT");
        String resourceProp = NWScript.getLocalString(objSelf, "RESOURCE_PROP");
        int abilityID;

        if(NWScript.getLocalInt(oPC, "NOT_USING_CORRECT_WEAPON") == 1)
        {
            NWScript.deleteLocalInt(oPC, "NOT_USING_CORRECT_WEAPON");
            return;
        }

        int baseDurabilityChance = 100;
        int durabilityChanceReduction;
        int weaponChanceBonus;
        boolean createSecondItem = false;

        if(activityID == 1) // 1 = Logging
        {
            abilityID = AbilityType.Lumberjack;
            durabilityChanceReduction = MagicSystem.IsAbilityEquipped(oPC, AbilityType.ToolExpertLogging) ? 50 : 0;
            weaponChanceBonus = NWScript.getLocalInt(oWeapon, "LOGGING_BONUS");

            if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.LumberCollector))
            {
                if(ThreadLocalRandom.current().nextInt(1, 100) <= 5)
                {
                    createSecondItem = true;
                }
            }

        }
        else if(activityID == 2) // Mining
        {
            abilityID = AbilityType.Miner;
            durabilityChanceReduction = MagicSystem.IsAbilityEquipped(oPC, AbilityType.ToolExpertMining) ? 50 : 0;
            weaponChanceBonus = NWScript.getLocalInt(oWeapon, "MINING_BONUS");

            if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.IronCollector))
            {
                if(ThreadLocalRandom.current().nextInt(1, 100) <= 5)
                {
                    createSecondItem = true;
                }
            }
        }
        else return;

        int durabilityLossChance = baseDurabilityChance - durabilityChanceReduction;

        if(ThreadLocalRandom.current().nextInt() <= durabilityLossChance)
        {
            DurabilitySystem.RunItemDecay(oPC, oWeapon);
        }

        int baseChance = 10;
        int bonusChance = MagicSystem.IsAbilityEquipped(oPC, abilityID) ? 10 : 0;
        int chance = baseChance + bonusChance + weaponChanceBonus;

        if(ThreadLocalRandom.current().nextInt(100) <= chance)
        {
            NWScript.createObject(ObjectType.ITEM, resourceItemResref, location, false, "");

            if(createSecondItem)
            {
                NWScript.createObject(ObjectType.ITEM, resourceItemResref, location, false, "");
            }


            NWScript.floatingTextStringOnCreature("You break off some " + resourceName + ".", oPC, false);
            NWScript.setLocalInt(objSelf, "RESOURCE_COUNT", --resourceCount);
            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(10000), objSelf, 0.0f);
        }

        if(resourceCount <= 0)
        {
            if(!Objects.equals(resourceProp, ""))
            {
                NWObject prop = NWScript.getNearestObjectByTag(resourceProp, objSelf, 1);
                NWScript.destroyObject(prop, 0.0f);
            }

            NWScript.destroyObject(objSelf, 0.0f);
        }
    }
}
