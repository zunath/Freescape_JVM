package GameSystems;

import Common.Constants;
import Data.Repository.PlayerRepository;
import Data.Repository.SkillRepository;
import Entities.*;
import Enumerations.*;
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

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class SkillSystem {

    private static HashMap<String, CreatureSkillRegistration> CreatureRegistrations = new HashMap<>();

    private static final float PrimaryIncrease = 0.2f;
    private static final float SecondaryIncrease = 0.1f;
    private static final float TertiaryIncrease = 0.05f;
    private static final int MaxAttributeBonus = 70;
    public static final int SkillCap = 500;

    public static void OnModuleEnter()
    {
        NWObject oPC = getEnteringObject();

        if(getIsPC(oPC) && !getIsDM(oPC) && !getIsDMPossessed(oPC))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            SkillRepository repo = new SkillRepository();
            repo.InsertAllPCSkillsByID(pcGO.getUUID());
            ForceEquipFistGlove(oPC);
        }
    }

    public static void OnModuleLeave()
    {
        NWObject oPC = getExitingObject();
        RemovePlayerFromRegistrations(oPC);
    }

    public static void OnModuleItemEquipped()
    {
        NWObject oPC = getPCItemLastEquippedBy();
        ApplyStatChanges(oPC, null);
    }

    public static void OnModuleItemUnequipped()
    {
        NWObject oPC = getPCItemLastUnequippedBy();
        NWObject oItem = getPCItemLastUnequipped();
        HandleGlovesUnequipEvent();
        ApplyStatChanges(oPC, oItem);
    }

    public static void OnAreaExit()
    {
        NWObject oPC = getExitingObject();
        RemovePlayerFromRegistrations(oPC);
    }

    private static void RemovePlayerFromRegistrations(NWObject oPC)
    {
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

    private static void HandleGlovesUnequipEvent()
    {
        NWObject oPC = getPCItemLastUnequippedBy();
        NWObject oItem = getPCItemLastUnequipped();
        int type = getBaseItemType(oItem);

        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;
        if(type != BaseItem.BRACER && type != BaseItem.GLOVES) return;

        // If fist was unequipped, destroy it.
        String resref = getResRef(oItem);
        if(resref.equals("fist"))
        {
            destroyObject(oItem, 0.0f);
        }

        // Check in 1 second to see if PC has a glove equipped. If they don't, create a fist glove and equip it.
        ForceEquipFistGlove(oPC);
    }

    private static void ForceEquipFistGlove(NWObject oPC)
    {
        Scheduler.delay(oPC, 1000, () -> {

            NWObject glove = getItemInSlot(InventorySlot.ARMS, oPC);
            if(!getIsObjectValid(glove))
            {
                clearAllActions(false);
                glove = createItemOnObject("fist", oPC, 1, "");
                actionEquipItem(glove, InventorySlot.ARMS);
                setLocalInt(glove, "UNBREAKABLE", 1);
            }
        });
    }

    private static int CalculateTotalSkillPointsPenalty(int totalSkillPoints, int xp)
    {
        if(totalSkillPoints >= 450)
        {
            xp = (int)(xp * 0.70f);
        }
        else if(totalSkillPoints >= 400)
        {
            xp = (int)(xp * 0.80f);
        }
        else if(totalSkillPoints >= 350)
        {
            xp = (int)(xp * 0.85f);
        }
        else if(totalSkillPoints >= 300)
        {
            xp = (int)(xp * 0.90f);
        }
        else if(totalSkillPoints >= 250)
        {
            xp = (int)(xp * 0.95f);
        }

        return xp;
    }

    public static void GiveSkillXP(NWObject oPC, int skillID, int xp)
    {
        if(skillID <= 0 || xp <= 0 || !getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        SkillRepository skillRepo = new SkillRepository();
        PlayerEntity player = playerRepo.GetByPlayerID(pcGO.getUUID());
        PCSkillEntity skill = skillRepo.GetPCSkillByID(pcGO.getUUID(), skillID);
        SkillXPRequirementEntity req = skillRepo.GetSkillXPRequirementByRank(skillID, skill.getRank());
        int maxRank = skillRepo.GetSkillMaxRank(skillID);
        int originalRank = skill.getRank();
        xp = CalculateTotalSkillPointsPenalty(player.getTotalSPAcquired(), xp);

        // Run the skill decay rules.
        // If the method returns false, that means all skills are locked.
        // So we can't give the player any XP.
        if(!ApplySkillDecay(oPC, skill, xp))
        {
            return;
        }

        skill.setXp(skill.getXp() + xp);
        sendMessageToPC(oPC, "You earned " + skill.getSkill().getName() + " skill experience. (" + xp + ")");

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
            floatingTextStringOnCreature("Your " + skill.getSkill().getName() + " skill level increased!", oPC, false);

            req = skillRepo.GetSkillXPRequirementByRank(skillID, skill.getRank());
        }

        skillRepo.Save(skill);


        // Update player and apply stat changes only if a level up occurred.
        if(originalRank != skill.getRank())
        {
            playerRepo.save(player);
            ApplyStatChanges(oPC, null);
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

        ApplyStatChanges(oPC, null);
        return true;
    }

    public static PCSkillEntity GetPCSkill(NWObject oPC, int skillID)
    {
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return null;
        PlayerGO pcGO = new PlayerGO(oPC);
        SkillRepository repo = new SkillRepository();
        return repo.GetPCSkillByID(pcGO.getUUID(), skillID);
    }

    private static int GetWeaponSkillID(NWObject item)
    {
        int skillID = -1;
        Integer type = getBaseItemType(item);
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
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;
        NWObject oSpellOrigin = getSpellCastItem();
        NWObject oTarget = getSpellTargetObject();

        int skillID = GetWeaponSkillID(oSpellOrigin);
        if(skillID <= -1) return;
        if(getIsPC(oTarget) || getIsDM(oTarget)) return;

        ItemGO itemGO = new ItemGO(oSpellOrigin);
        CreatureGO creatureGO = new CreatureGO(oTarget);
        CreatureSkillRegistration reg = GetCreatureSkillRegistration(creatureGO.getGlobalUUID());

        reg.AddSkillRegistrationPoint(oPC, skillID, itemGO.getRecommendedLevel());

        // Add a registration point if a shield is equipped. This is to prevent players from swapping out a weapon for a shield
        // just before they kill an enemy.
        NWObject oShield = getItemInSlot(InventorySlot.LEFTHAND, oPC);
        int offHandType = getBaseItemType(oShield);
        if(offHandType == BaseItem.SMALLSHIELD ||
                offHandType == BaseItem.LARGESHIELD ||
                offHandType == BaseItem.TOWERSHIELD)
        {
            itemGO = new ItemGO(oShield);
            reg.AddSkillRegistrationPoint(oPC, SkillID.Shields, itemGO.getRecommendedLevel());
        }

        FoodSystem.DecreaseHungerLevel(oPC, 1);
    }

    public static void RegisterPCToNPCForSkill(NWObject pc, NWObject npc, int skillID)
    {
        if(!getIsPC(pc) || getIsDM(pc) || getIsDMPossessed(pc) || !getIsObjectValid(pc)) return;
        if(getIsPC(npc) || getIsDM(npc) || !getIsObjectValid(npc)) return;
        if(skillID <= 0) return;

        PCSkillEntity pcSkill = GetPCSkill(pc, skillID);
        if(pcSkill == null) return;

        CreatureGO npcGO = new CreatureGO(npc);
        CreatureSkillRegistration reg = GetCreatureSkillRegistration(npcGO.getGlobalUUID());
        reg.AddSkillRegistrationPoint(pc, skillID, pcSkill.getRank());
    }

    public static void RegisterPCToAllCombatTargetsForSkill(NWObject pc, int skillID)
    {
        if(!getIsPC(pc) || getIsDM(pc) || getIsDMPossessed(pc)) return;
        if(skillID <= 0) return;

        NWObject[] members = getFactionMembers(pc, true);
        for(NWObject member: members)
        {
            NWObject target = getAttackTarget(member);
            if(!getIsObjectValid(target)) continue;
            if(!getArea(target).equals(getArea(pc))) continue;

            RegisterPCToNPCForSkill(pc, target, skillID);
        }
    }

    public static float CalculateSkillAdjustedXP(float xp, int registeredLevel, int skillRank)
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
            if(!getIsObjectValid(preg.getPC()) ||
                    !getResRef(getArea(preg.getPC())).equals(getResRef(getArea(creature))) ||
                    getDistanceBetween(preg.getPC(), creature) > 30.0f)
                continue;

            PlayerGO pcGO = new PlayerGO(preg.getPC());
            float cr = NWScript.getChallengeRating(creature);
            float baseXP = cr * 400 + ThreadLocalRandom.current().nextInt(20);
            float moduleXPAdjustment = getLocalFloat(getModule(), "SKILL_SYSTEM_MODULE_XP_MODIFIER");
            if(moduleXPAdjustment <= 0.0f) moduleXPAdjustment = 1.0f;
            baseXP = baseXP * moduleXPAdjustment;

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

            ItemGO itemGO = new ItemGO(getItemInSlot(InventorySlot.CHEST, preg.getPC()));
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
    public static void ApplyStatChanges(NWObject oPC, NWObject ignoreItem)
    {
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;
        if(!PlayerInitializationSystem.IsPCInitialized(oPC)) return;

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

        // Apply AC
        int ac = CalculateItemAC(oPC, ignoreItem) + pcGO.CalculateEffectAC(oPC);
        NWNX_Creature.SetBaseAC(oPC, ac);

        // Apply BAB
        int bab = CalculateBAB(oPC, ignoreItem);
        NWNX_Creature.SetBaseAttackBonus(oPC, bab);

        // Apply HP
        int hp = 30 + getAbilityModifier(Ability.CONSTITUTION, oPC)  * 5;
        hp += PerkSystem.GetPCPerkLevel(oPC, PerkID.Health) * 5;
        if(hp > 255) hp = 255;
        if(hp < 20) hp = 20;
        NWNX_Creature.SetMaxHitPointsByLevel(oPC, 1, hp);

        // Apply Mana
        int mana = 20;
        mana += (getAbilityModifier(Ability.INTELLIGENCE, oPC) +
                getAbilityModifier(Ability.WISDOM, oPC) +
                getAbilityModifier(Ability.CHARISMA, oPC)) * 5;
        mana += PerkSystem.GetPCPerkLevel(oPC, PerkID.Mana) * 5;
        if(mana < 0) mana = 0;
        pcEntity.setMaxMana(mana);

        playerRepo.save(pcEntity);
    }

    private static int CalculateBAB(NWObject oPC, NWObject ignoreItem)
    {
        NWObject weapon = getItemInSlot(InventorySlot.RIGHTHAND, oPC);

        // The unequip event fires before the item is actually unequipped, so we need
        // to have additional checks to make sure we're not getting the weapon that's about to be
        // unequipped.
        if(weapon.equals(ignoreItem))
        {
            weapon = NWObject.INVALID;
            NWObject offHand = getItemInSlot(InventorySlot.LEFTHAND, oPC);
            ItemGO offGO = new ItemGO(offHand);
            if(offGO.IsBlade() ||
               offGO.IsFinesseBlade() ||
               offGO.IsBlunt() ||
               offGO.IsHeavyBlade() ||
               offGO.IsHeavyBlunt() ||
               offGO.IsPolearm() ||
               offGO.IsTwinBlade() ||
               offGO.IsMartialArtsWeapon() ||
               offGO.IsBow() ||
               offGO.IsCrossbow() ||
               offGO.IsThrowing())
            {
                weapon = offHand;
            }
        }

        if(!getIsObjectValid(weapon))
        {
            weapon = getItemInSlot(InventorySlot.ARMS, oPC);
        }
        if(!getIsObjectValid(weapon)) return 0;

        int weaponSkillID = GetWeaponSkillID(weapon);
        PCSkillEntity skill = GetPCSkill(oPC, weaponSkillID);
        if(skill == null) return 0;
        int skillBAB = skill.getRank() / 10;
        int perkBAB = 0;

        if(weaponSkillID == SkillID.Throwing)
        {
            perkBAB += PerkSystem.GetPCPerkLevel(oPC, PerkID.TossAccuracy);
        }

        return 1 + skillBAB + perkBAB; // Note: Always add 1 to BAB. 0 will cause a crash in NWNX.
    }

    private static int CalculateItemAC(NWObject oPC, NWObject ignoreItem)
    {
        int ac = 0;
        for(int slot = 0; slot < Constants.NumberOfInventorySlots; slot++)
        {
            NWObject oItem = getItemInSlot(slot, oPC);
            if(oItem.equals(ignoreItem))
                continue;

            ItemGO itemGO = new ItemGO(oItem);
            if(!oItem.equals(NWObject.INVALID))
            {
                int itemAC = itemGO.getAC();

                ac += itemAC;
            }
        }
        return ac;
    }


}
