package GameSystems;

import Data.Repository.PlayerRepository;
import Data.Repository.SkillRepository;
import Entities.*;
import Enumerations.CustomAttributeType;
import Enumerations.CustomItemType;
import Enumerations.SkillID;
import GameObject.*;
import NWNX.NWNX_Creature;
import com.sun.tools.javac.util.Pair;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SkillSystem {

    private static HashMap<String, CreatureSkillRegistration> CreatureRegistrations = new HashMap<>();

    private static final float PrimaryIncrease = 0.2f;
    private static final float SecondaryIncrease = 0.1f;
    private static final float TertiaryIncrease = 0.05f;
    private static final int MaxAttributeBonus = 70;
    public static final int SkillCap = 500;

    public static void OnModuleEnter()
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(NWScript.getIsPC(oPC) && !NWScript.getIsDM(oPC) && !NWScript.getIsDMPossessed(oPC))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            SkillRepository repo = new SkillRepository();
            repo.InsertAllPCSkillsByID(pcGO.getUUID());
            ForceEquipFistGlove(oPC);
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

    public static void OnModuleItemUnequipped()
    {
        NWObject oPC = NWScript.getPCItemLastUnequippedBy();
        NWObject oItem = NWScript.getPCItemLastUnequipped();
        int type = NWScript.getBaseItemType(oItem);

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;
        if(type != BaseItem.BRACER && type != BaseItem.GLOVES) return;

        // If fist was unequipped, destroy it.
        String resref = NWScript.getResRef(oItem);
        if(resref.equals("fist"))
        {
            NWScript.destroyObject(oItem, 0.0f);
        }

        // Check in 1 second to see if PC has a glove equipped. If they don't, create a fist glove and equip it.
        ForceEquipFistGlove(oPC);
    }

    private static void ForceEquipFistGlove(NWObject oPC)
    {
        Scheduler.delay(oPC, 1000, () -> {

            NWObject glove = NWScript.getItemInSlot(InventorySlot.ARMS, oPC);
            if(!NWScript.getIsObjectValid(glove))
            {
                NWScript.clearAllActions(false);
                glove = NWScript.createItemOnObject("fist", oPC, 1, "");
                NWScript.actionEquipItem(glove, InventorySlot.ARMS);
                NWScript.setLocalInt(glove, "UNBREAKABLE", 1);
            }
        });
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
        int originalRank = skill.getRank();

        // Run the skill decay rules.
        // If the method returns false, that means all skills are locked.
        // So we can't give the player any XP.
        if(!ApplySkillDecay(oPC, skill, xp))
        {
            return;
        }

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

            if(player.getTotalSPAcquired() < SkillCap)
            {
                player.setUnallocatedSP(player.getUnallocatedSP() + 1);
                player.setTotalSPAcquired(player.getTotalSPAcquired() + 1);
            }
            skill.setRank(skill.getRank() + 1);
            NWScript.floatingTextStringOnCreature("Your " + skill.getSkill().getName() + " skill level increased!", oPC, false);

            req = skillRepo.GetSkillXPRequirementByRank(skillID, skill.getRank());
        }

        skillRepo.Save(skill);


        // Update player and apply stat changes only if a level up occurred.
        if(originalRank != skill.getRank())
        {
            playerRepo.save(player);
            ApplyStatChanges(oPC);
        }
    }

    private static boolean ApplySkillDecay(NWObject oPC, PCSkillEntity levelingSkill, int xp)
    {
        SkillRepository skillRepo = new SkillRepository();
        int totalSkillRanks = skillRepo.GetPCTotalSkillCount(levelingSkill.getPlayerID());
        if(totalSkillRanks < SkillCap) return true;

        // Find out if we have enough XP to remove. If we don't, make no changes and return false signifying no XP could be removed.
        List<TotalSkillXPEntity> skillTotalXP = skillRepo.GetTotalXPAmountsForPC(levelingSkill.getPlayerID(), levelingSkill.getSkill().getSkillID());
        int aggregateXP = 0;
        for(TotalSkillXPEntity p : skillTotalXP)
        {
            aggregateXP += p.getTotalSkillXP();
        }
        if(aggregateXP < xp) return false;

        // We have enough XP to remove. Reduce XP, picking random skills each time we reduce.
        List<PCSkillEntity> skillsPossibleToDecay = skillRepo.GetAllUnlockedPCSkills(levelingSkill.getPlayerID(), levelingSkill.getSkill().getSkillID());
        while(xp > 0)
        {
            int skillIndex = ThreadLocalRandom.current().nextInt(skillsPossibleToDecay.size());
            PCSkillEntity decaySkill = skillsPossibleToDecay.get(skillIndex);
            int totalDecaySkillXP = skillTotalXP.get(skillIndex).getTotalSkillXP();

            if(totalDecaySkillXP >= xp)
            {
                totalDecaySkillXP = totalDecaySkillXP - xp;
                xp = 0;
            }
            else if(totalDecaySkillXP < xp)
            {
                totalDecaySkillXP = 0;
                xp = xp - totalDecaySkillXP;
            }

            // If skill drops to 0 total XP, remove it from the possible list of skills
            if(totalDecaySkillXP <= 0)
            {
                skillsPossibleToDecay.remove(decaySkill);
                decaySkill.setXp(0);
                decaySkill.setRank(0);
            }
            // Otherwise calculate what rank and XP value the skill should now be.
            else
            {
                List<SkillXPRequirementEntity> reqs = skillRepo.GetSkillXPRequirementsUpToRank(decaySkill.getSkill().getSkillID(), decaySkill.getRank());
                int newDecaySkillRank = 0;
                for(SkillXPRequirementEntity req: reqs)
                {
                    if(totalDecaySkillXP >= req.getXp())
                    {
                        totalDecaySkillXP = totalDecaySkillXP - req.getXp();
                        newDecaySkillRank++;
                    }
                    else if(totalDecaySkillXP < req.getXp())
                    {
                        break;
                    }
                }

                decaySkill.setRank(newDecaySkillRank);
                decaySkill.setXp(totalDecaySkillXP);
            }

            skillRepo.Save(decaySkill);
        }

        ApplyStatChanges(oPC);
        // TODO: Figure out SP refunds.
        return true;
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

    private static CreatureSkillRegistration GetCreatureSkillRegistration(String creatureUUID)
    {
        CreatureSkillRegistration reg = CreatureRegistrations.getOrDefault(creatureUUID, null);
        if(reg == null)
        {
            reg = new CreatureSkillRegistration(creatureUUID);
            CreatureRegistrations.put(creatureUUID, reg);
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

        ItemGO itemGO = new ItemGO(oSpellOrigin);
        CreatureGO creatureGO = new CreatureGO(oTarget);
        CreatureSkillRegistration reg = GetCreatureSkillRegistration(creatureGO.getGlobalUUID());

        reg.AddSkillRegistrationPoint(oPC, skillID, itemGO.getRecommendedLevel());

        // Add a registration point if a shield is equipped. This is to prevent players from swapping out a weapon for a shield
        // just before they kill an enemy.
        NWObject oShield = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);
        int offHandType = NWScript.getBaseItemType(oShield);
        if(offHandType == BaseItem.SMALLSHIELD ||
                offHandType == BaseItem.LARGESHIELD ||
                offHandType == BaseItem.TOWERSHIELD)
        {
            itemGO = new ItemGO(oShield);
            reg.AddSkillRegistrationPoint(oPC, SkillID.Shields, itemGO.getRecommendedLevel());
        }
    }

    private static float CalculateSkillAdjustedXP(float xp, int registeredLevel, int skillRank)
    {
        int delta = registeredLevel - skillRank;
        float levelAdjustment = 0.2f * delta;

        if(levelAdjustment > 1.0f) levelAdjustment = 1.0f;
        if(levelAdjustment < -1.0f) levelAdjustment = -1.0f;

        xp = xp + (xp * levelAdjustment);
        return xp;
    }

    public static void OnCreatureDeath(NWObject creature)
    {
        CreatureGO creatureGO = new CreatureGO(creature);
        CreatureSkillRegistration reg = GetCreatureSkillRegistration(creatureGO.getGlobalUUID());
        PlayerSkillRegistration[] playerRegs = reg.GetAllRegistrations();
        SkillRepository skillRepo = new SkillRepository();

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

            PlayerGO pcGO = new PlayerGO(preg.getPC());
            float baseXP = creatureGO.getDifficultyRating() * 250 + ThreadLocalRandom.current().nextInt(20);

            if(creatureGO.getXPModifier() != 0.0f)
            {
                baseXP = baseXP * creatureGO.getXPModifier();
            }

            ArrayList<Pair<Integer, PlayerSkillPointTracker>> skillRegs = preg.GetSkillRegistrationPoints();
            int totalPoints = preg.GetTotalSkillRegistrationPoints();

            boolean receivesMartialArtsPenalty = CheckForMartialArtsPenalty(skillRegs);

            // Grant XP based on points acquired during combat.
            for(Pair<Integer, PlayerSkillPointTracker> skreg: skillRegs)
            {
                int skillID = skreg.fst;
                int skillRank = skillRepo.GetPCSkillByID(pcGO.getUUID(), skillID).getRank();
                int points = skreg.snd.getPoints();
                float percentage = (float)points / (float)totalPoints;
                float adjustedXP = baseXP * percentage;
                adjustedXP = CalculateSkillAdjustedXP(adjustedXP, skreg.snd.getRegisteredLevel(), skillRank);

                // Penalty to martial arts XP for using a shield.
                if(skillID == SkillID.MartialArts && receivesMartialArtsPenalty)
                    adjustedXP = adjustedXP * 0.4f;

                GiveSkillXP(preg.getPC(), skillID, (int)(adjustedXP));
            }

            ItemGO itemGO = new ItemGO(NWScript.getItemInSlot(InventorySlot.CHEST, preg.getPC()));
            if(itemGO.getCustomItemType() == CustomItemType.LightArmor)
            {
                int skillRank = skillRepo.GetPCSkillByID(pcGO.getUUID(), SkillID.LightArmor).getRank();
                float adjustedXP = baseXP * 0.10f;
                adjustedXP = CalculateSkillAdjustedXP(adjustedXP, itemGO.getRecommendedLevel(), skillRank);
                GiveSkillXP(preg.getPC(), SkillID.LightArmor, (int)adjustedXP);
            }
            else if(itemGO.getCustomItemType() == CustomItemType.HeavyArmor)
            {
                int skillRank = skillRepo.GetPCSkillByID(pcGO.getUUID(), SkillID.HeavyArmor).getRank();
                float adjustedXP = baseXP * 0.10f;
                adjustedXP = CalculateSkillAdjustedXP(adjustedXP, itemGO.getRecommendedLevel(), skillRank);
                GiveSkillXP(preg.getPC(), SkillID.HeavyArmor, (int)adjustedXP);
            }
        }

        CreatureRegistrations.remove(creatureGO.getGlobalUUID());
    }

    private static boolean CheckForMartialArtsPenalty(ArrayList<Pair<Integer, PlayerSkillPointTracker>> skillRegs)
    {
        boolean usedShield = false;
        boolean usedMartialArts = false;
        for(Pair<Integer, PlayerSkillPointTracker> sreg : skillRegs)
        {
            if(sreg.fst == SkillID.Shields) usedShield = true;
            else if(sreg.fst == SkillID.MartialArts) usedMartialArts = true;

            if(usedMartialArts && usedShield) return true;
        }

        return false;
    }

    @SuppressWarnings("ConstantConditions")
    private static void ApplyStatChanges(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        SkillRepository skillRepo = new SkillRepository();
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerEntity pcEntity = playerRepo.GetByPlayerID(pcGO.getUUID());
        List<PCSkillEntity> skills = skillRepo.GetAllPCSkills(pcGO.getUUID());
        float strBonus = 0.0f;
        float dexBonus = 0.0f;
        float conBonus = 0.0f;
        float intBonus = 0.0f;
        float wisBonus = 0.0f;
        float chaBonus = 0.0f;

        for(PCSkillEntity pcSkill: skills)
        {
            SkillEntity skill = pcSkill.getSkill();
            int primary = skill.getPrimary();
            int secondary = skill.getSecondary();
            int tertiary = skill.getTertiary();

            // Primary Bonuses
            if(primary == CustomAttributeType.STR) strBonus += PrimaryIncrease * pcSkill.getRank();
            else if(primary == CustomAttributeType.DEX) dexBonus += PrimaryIncrease * pcSkill.getRank();
            else if(primary == CustomAttributeType.CON) conBonus += PrimaryIncrease * pcSkill.getRank();
            else if(primary == CustomAttributeType.INT) intBonus += PrimaryIncrease * pcSkill.getRank();
            else if(primary == CustomAttributeType.WIS) wisBonus += PrimaryIncrease * pcSkill.getRank();
            else if(primary == CustomAttributeType.CHA) chaBonus += PrimaryIncrease * pcSkill.getRank();

            // Secondary Bonuses
            if(secondary == CustomAttributeType.STR) strBonus += SecondaryIncrease * pcSkill.getRank();
            else if(secondary == CustomAttributeType.DEX) dexBonus += SecondaryIncrease * pcSkill.getRank();
            else if(secondary == CustomAttributeType.CON) conBonus += SecondaryIncrease * pcSkill.getRank();
            else if(secondary == CustomAttributeType.INT) intBonus += SecondaryIncrease * pcSkill.getRank();
            else if(secondary == CustomAttributeType.WIS) wisBonus += SecondaryIncrease * pcSkill.getRank();
            else if(secondary == CustomAttributeType.CHA) chaBonus += SecondaryIncrease * pcSkill.getRank();

            // Tertiary Bonuses
            if(tertiary == CustomAttributeType.STR) strBonus += TertiaryIncrease * pcSkill.getRank();
            else if(tertiary == CustomAttributeType.DEX) dexBonus += TertiaryIncrease * pcSkill.getRank();
            else if(tertiary == CustomAttributeType.CON) conBonus += TertiaryIncrease * pcSkill.getRank();
            else if(tertiary == CustomAttributeType.INT) intBonus += TertiaryIncrease * pcSkill.getRank();
            else if(tertiary == CustomAttributeType.WIS) wisBonus += TertiaryIncrease * pcSkill.getRank();
            else if(tertiary == CustomAttributeType.CHA) chaBonus += TertiaryIncrease * pcSkill.getRank();
        }

        // Check caps.
        if(strBonus > MaxAttributeBonus) strBonus = MaxAttributeBonus;
        if(dexBonus > MaxAttributeBonus) dexBonus = MaxAttributeBonus;
        if(conBonus > MaxAttributeBonus) conBonus = MaxAttributeBonus;
        if(intBonus > MaxAttributeBonus) intBonus = MaxAttributeBonus;
        if(wisBonus > MaxAttributeBonus) wisBonus = MaxAttributeBonus;
        if(chaBonus > MaxAttributeBonus) chaBonus = MaxAttributeBonus;

        // Apply attributes
        NWNX_Creature.SetRawAbilityScore(oPC, Ability.STRENGTH, (int)strBonus + pcEntity.getStrBase());
        NWNX_Creature.SetRawAbilityScore(oPC, Ability.DEXTERITY, (int)dexBonus + pcEntity.getDexBase());
        NWNX_Creature.SetRawAbilityScore(oPC, Ability.CONSTITUTION, (int)conBonus + pcEntity.getConBase());
        NWNX_Creature.SetRawAbilityScore(oPC, Ability.INTELLIGENCE, (int)intBonus + pcEntity.getIntBase());
        NWNX_Creature.SetRawAbilityScore(oPC, Ability.WISDOM, (int)wisBonus + pcEntity.getWisBase());
        NWNX_Creature.SetRawAbilityScore(oPC, Ability.CHARISMA, (int)chaBonus + pcEntity.getChaBase());

    }

}
