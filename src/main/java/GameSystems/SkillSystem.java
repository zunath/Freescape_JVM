package GameSystems;

import Data.Repository.PlayerRepository;
import Data.Repository.SkillRepository;
import Entities.PCSkillEntity;
import Entities.PlayerEntity;
import Entities.SkillXPRequirementEntity;
import Enumerations.CustomItemType;
import Enumerations.SkillID;
import GameObject.*;
import com.sun.tools.javac.util.Pair;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SkillSystem {

    private static HashMap<String, CreatureSkillRegistration> CreatureRegistrations = new HashMap<>();

    public static void OnModuleEnter()
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(NWScript.getIsPC(oPC) && !NWScript.getIsDM(oPC) && !NWScript.getIsDMPossessed(oPC))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            SkillRepository repo = new SkillRepository();
            repo.InsertAllPCSkillsByID(pcGO.getUUID());
        }
    }

    public static void OnModuleLeave()
    {
        NWObject oPC = NWScript.getExitingObject();

        for(CreatureSkillRegistration reg: CreatureRegistrations.values())
        {
            reg.RemovePlayerRegistration(oPC);

            if(reg.IsRegistrationEmpty())
            {
                CreatureRegistrations.remove(reg.GetCreatureID());
            }
            else
            {
                CreatureRegistrations.put(reg.GetCreatureID(), reg);
            }
        }

    }

    public static void GiveSkillXP(NWObject oPC, int skillID, int xp)
    {
        if(xp <= 0 || !NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        SkillRepository skillRepo = new SkillRepository();
        PlayerEntity player = playerRepo.GetByPlayerID(pcGO.getUUID());
        PCSkillEntity skill = skillRepo.GetPCSkillByID(pcGO.getUUID(), skillID);
        SkillXPRequirementEntity req = skillRepo.GetSkillXPRequirementByRank(skillID, skill.getRank());
        int maxRank = skillRepo.GetSkillMaxRank(skillID);

        skill.setXp(skill.getXp() + xp);
        NWScript.sendMessageToPC(oPC, "You earned " + skill.getSkill().getName() + " skill experience.");

        // Skill is at cap and player would level up.
        // Reduce XP to required amount minus 1 XP
        if(skill.getRank() >= maxRank && skill.getXp() > req.getXp())
        {
            skill.setXp(skill.getXp() - 1);
        }

        while(skill.getXp() >= req.getXp())
        {
            skill.setXp(skill.getXp() - req.getXp());
            player.setUnallocatedSP(player.getUnallocatedSP() + 1);
            skill.setRank(skill.getRank() + 1);
            NWScript.floatingTextStringOnCreature("Your " + skill.getSkill().getName() + " skill level increased!", oPC, false);

            req = skillRepo.GetSkillXPRequirementByRank(skillID, skill.getRank());
        }

        skillRepo.Save(skill);
        playerRepo.save(player);
    }

    public static PCSkillEntity GetPCSkill(NWObject oPC, int skillID)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return null;
        PlayerGO pcGO = new PlayerGO(oPC);
        SkillRepository repo = new SkillRepository();
        return repo.GetPCSkillByID(pcGO.getUUID(), skillID);
    }

    private static int GetWeaponSkillID(NWObject item)
    {
        int skillID = -1;
        Integer type = NWScript.getBaseItemType(item);
        Integer[] oneHandedTypes = {
                BaseItem.BASTARDSWORD,
                BaseItem.BATTLEAXE,
                BaseItem.CLUB,
                BaseItem.DAGGER,
                BaseItem.HANDAXE,
                BaseItem.KAMA,
                BaseItem.KATANA,
                BaseItem.KUKRI,
                BaseItem.LIGHTFLAIL,
                BaseItem.LIGHTHAMMER,
                BaseItem.LIGHTMACE,
                BaseItem.LONGSWORD,
                BaseItem.RAPIER,
                BaseItem.SCIMITAR,
                BaseItem.SHORTSPEAR,
                BaseItem.SHORTSWORD,
                BaseItem.SICKLE,
                BaseItem.WHIP
        };

        Integer[] twoHandedTypes = {
                BaseItem.DIREMACE,
                BaseItem.DWARVENWARAXE,
                BaseItem.GREATAXE,
                BaseItem.GREATSWORD,
                BaseItem.HALBERD,
                BaseItem.HEAVYFLAIL,
                BaseItem.MORNINGSTAR,
                BaseItem.QUARTERSTAFF,
                BaseItem.SCYTHE,
                BaseItem.TRIDENT,
                BaseItem.WARHAMMER
        };

        Integer[] twinBladeTypes = {
                BaseItem.DOUBLEAXE,
                BaseItem.TWOBLADEDSWORD
        };

        Integer[] martialArtsTypes = {
                BaseItem.BRACER,
                BaseItem.GLOVES
        };

        Integer[] archeryTypes = {
                BaseItem.HEAVYCROSSBOW,
                BaseItem.LIGHTCROSSBOW,
                BaseItem.LONGBOW,
                BaseItem.SHORTBOW,
                BaseItem.ARROW,
                BaseItem.BOLT
        };

        Integer[] throwingTypes = {
                BaseItem.GRENADE,
                BaseItem.SHURIKEN,
                BaseItem.SLING,
                BaseItem.THROWINGAXE,
                BaseItem.BULLET,
                BaseItem.DART
        };

        if(Arrays.asList(oneHandedTypes).contains(type)) skillID = SkillID.OneHanded;
        else if(Arrays.asList(twoHandedTypes).contains(type)) skillID = SkillID.TwoHanded;
        else if(Arrays.asList(twinBladeTypes).contains(type)) skillID = SkillID.TwinBlades;
        else if(Arrays.asList(martialArtsTypes).contains(type)) skillID = SkillID.MartialArts;
        else if(Arrays.asList(archeryTypes).contains(type)) skillID = SkillID.Archery;
        else if(Arrays.asList(throwingTypes).contains(type)) skillID = SkillID.Throwing;

        return skillID;
    }

    private static CreatureSkillRegistration GetCreatureSkillRegistration(String creatureID)
    {
        CreatureSkillRegistration reg = CreatureRegistrations.getOrDefault(creatureID, null);
        if(reg == null)
        {
            reg = new CreatureSkillRegistration(creatureID);
            CreatureRegistrations.put(creatureID, reg);
        }

        return reg;
    }

    public static void OnHitCastSpell(NWObject oPC)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;
        NWObject oSpellOrigin = NWScript.getSpellCastItem();
        NWObject oTarget = NWScript.getSpellTargetObject();

        int skillID = GetWeaponSkillID(oSpellOrigin);
        if(skillID <= -1) return;
        if(NWScript.getIsPC(oTarget) || NWScript.getIsDM(oTarget)) return;

        CreatureGO creatureGO = new CreatureGO(oTarget);
        CreatureSkillRegistration reg = GetCreatureSkillRegistration(creatureGO.getUUID());

        reg.AddSkillRegistrationPoint(oPC, skillID);
    }

    private static float CalculateCreatureXP(NWObject oPC, NWObject creature)
    {
        return 100; // TODO: Calculate based on difficulty, player comprehensive strength, etc.
    }

    public static void OnCreatureDeath(NWObject creature)
    {
        CreatureGO creatureGO = new CreatureGO(creature);
        CreatureSkillRegistration reg = GetCreatureSkillRegistration(creatureGO.getUUID());
        PlayerSkillRegistration[] playerRegs = reg.GetAllRegistrations();

        for(PlayerSkillRegistration preg : playerRegs)
        {
            // Rules for acquiring skill XP:
            // Player must be valid.
            // Player must be in the same area as the creature that just died.
            // Player must be within 30 meters of the creature that just died.
            if(!NWScript.getIsObjectValid(preg.getPC()) ||
                    !NWScript.getResRef(NWScript.getArea(preg.getPC())).equals(NWScript.getResRef(NWScript.getArea(creature))) ||
                    NWScript.getDistanceBetween(preg.getPC(), creature) > 30.0f)
                continue;

            float xp = CalculateCreatureXP(preg.getPC(), creature);
            ArrayList<Pair<Integer, Integer>> skillRegs = preg.GetSkillRegistrationPoints();
            int totalPoints = preg.GetTotalSkillRegistrationPoints();

            // Grant XP based on points acquired during combat.
            for(Pair<Integer, Integer> skreg: skillRegs)
            {
                int skillID = skreg.fst;
                int points = skreg.snd;
                float percentage = (float)points / (float)totalPoints;

                GiveSkillXP(preg.getPC(), skillID, (int)(xp * percentage));
            }

            // Static 10% xp to armor and shields.
            int offHandType = NWScript.getBaseItemType(NWScript.getItemInSlot(InventorySlot.LEFTHAND, preg.getPC()));
            if(offHandType == BaseItem.SMALLSHIELD ||
                    offHandType == BaseItem.LARGESHIELD ||
                    offHandType == BaseItem.TOWERSHIELD)
            {
                GiveSkillXP(preg.getPC(), SkillID.Shields, (int)(xp * 0.10f));
            }

            ItemGO itemGO = new ItemGO(NWScript.getItemInSlot(InventorySlot.CHEST, preg.getPC()));
            if(itemGO.GetCustomItemType() == CustomItemType.LightArmor)
            {
                GiveSkillXP(preg.getPC(), SkillID.LightArmor, (int)(xp * 0.10f));
            }
            else if(itemGO.GetCustomItemType() == CustomItemType.HeavyArmor)
            {
                GiveSkillXP(preg.getPC(), SkillID.HeavyArmor, (int)(xp * 0.10f));
            }
        }

        CreatureRegistrations.remove(creatureGO.getUUID());
    }
}
