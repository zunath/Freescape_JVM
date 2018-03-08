package GameSystems;

import Abilities.IAbility;
import Bioware.Position;
import Data.Repository.MagicRepository;
import Data.Repository.PlayerRepository;
import Entities.*;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ScriptHelper;
import Helper.TimeHelper;
import NWNX.NWNX_Creature;
import NWNX.NWNX_Events;
import NWNX.NWNX_Player;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.NWVector;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.Objects;
import java.util.UUID;

public class MagicSystem {

    private static int SPELL_STATUS_STARTED = 1;
    private static int SPELL_STATUS_INTERRUPTED = 2;

    public static void OnFeatUsed(NWObject pc)
    {
        PlayerRepository playerRepo = new PlayerRepository();
        MagicRepository repo = new MagicRepository();
        PlayerGO pcGO = new PlayerGO(pc);
        int featID = NWNX_Events.OnFeatUsed_GetFeatID();
        NWObject target =  NWNX_Events.OnFeatUsed_GetTarget();
        AbilityEntity entity = repo.GetAbilityByFeatID(featID);

        if(entity == null) return;
        IAbility ability = (IAbility) ScriptHelper.GetClassByName("Abilities." + entity.getJavaScriptName());
        if(ability == null) return;

        PlayerEntity playerEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        if(ability.IsHostile())
        {
            if(!PVPSanctuarySystem.IsPVPAttackAllowed(pc, target)) return;
        }

        if(!Objects.equals(NWScript.getResRef(NWScript.getArea(pc)), NWScript.getResRef(NWScript.getArea(target))) ||
                NWScript.lineOfSightObject(pc, target) == 0)
        {
            NWScript.sendMessageToPC(pc, "You cannot see your target.");
            return;
        }

        if(!ability.CanCastSpell(pc, target))
        {
            NWScript.sendMessageToPC(pc,
                    ability.CannotCastSpellMessage() == null ?
                            "That ability cannot be used at this time." :
                            ability.CannotCastSpellMessage());
            return;
        }

        int manaCost = ability.ManaCost(pc, ability.ManaCost(pc, entity.getBaseManaCost()));
        if(playerEntity.getCurrentMana() < manaCost)
        {
            NWScript.sendMessageToPC(pc, "You do not have enough mana. (Required: " + manaCost + ". You have: " + playerEntity.getCurrentMana() + ")");
            return;
        }

        if(pcGO.isBusy() || NWScript.getCurrentHitPoints(pc) <= 0)
        {
            NWScript.sendMessageToPC(pc, "You are too busy to activate that ability.");
            return;
        }

        // Check cooldown
        PCAbilityCooldownEntity pcCooldown = repo.GetPCCooldownByID(pcGO.getUUID(), entity.getCooldown().getAbilityCooldownCategoryID());
        DateTime unlockDateTime = new DateTime(pcCooldown.getDateUnlocked());
        DateTime now = DateTime.now(DateTimeZone.UTC);

        if(unlockDateTime.isAfter(now))
        {
            String timeToWait = TimeHelper.GetTimeToWaitLongIntervals(now, unlockDateTime, false);
            NWScript.sendMessageToPC(pc, "That ability can be used again in " + timeToWait + ".");
            return;
        }


        if(ability.CastingTime(pc, entity.getBaseCastingTime()) > 0.0f)
        {
            CastSpell(pc, target, entity, ability, entity.getCooldown());
        }
        else
        {
            if(!entity.isQueuedWeaponSkill())
            {
                ability.OnImpact(pc, target);

                if(manaCost > 0)
                {
                    playerEntity.setCurrentMana(playerEntity.getCurrentMana() - manaCost);
                    playerRepo.save(playerEntity);
                }
            }
            else
            {
                HandleQueueWeaponSkill(pc, entity, ability);
            }

        }
    }

    private static void CastSpell(final NWObject pc,
                                  final NWObject target,
                                  final AbilityEntity entity,
                                  final IAbility ability,
                                  final AbilityCooldownCategoryEntity cooldown)
    {
        final String spellUUID = UUID.randomUUID().toString();
        final PlayerGO pcGO = new PlayerGO(pc);
        int itemBonus = pcGO.CalculateCastingSpeed();
        float castingTime = ability.CastingTime(pc, entity.getBaseCastingTime());

        // Casting Bonus % - Shorten casting time.
        if(itemBonus < 0)
        {
            float castingPercentageBonus = Math.abs(itemBonus) * 0.01f;
            castingTime = castingTime - (castingTime * castingPercentageBonus);
        }
        // Casting Penalty % - Increase casting time.
        else if(itemBonus > 0)
        {
            float castingPercentageBonus = Math.abs(itemBonus) * 0.01f;
            castingTime = castingTime + (castingTime * castingPercentageBonus);
        }

        if(castingTime < 0.5f)
            castingTime = 0.5f;


        if(NWScript.getActionMode(pc, ActionMode.STEALTH))
            NWScript.setActionMode(pc, ActionMode.STEALTH, false);

        NWScript.clearAllActions(false);
        Position.TurnToFaceObject(target, pc);
        NWScript.applyEffectToObject(DurationType.TEMPORARY,
                NWScript.effectVisualEffect(VfxDur.ELEMENTAL_SHIELD, false),
                pc,
                castingTime + 0.2f);
        NWScript.actionPlayAnimation(Animation.LOOPING_CONJURE1, 1.0f, castingTime - 0.1f);

        pcGO.setIsBusy(true);
        CheckForSpellInterruption(pc, spellUUID, NWScript.getPosition(pc));
        NWScript.setLocalInt(pc, spellUUID, SPELL_STATUS_STARTED);

        NWNX_Player.StartGuiTimingBar(pc, (int)castingTime, "");
        Scheduler.delay(pc, (int)(1050 * castingTime), () -> {
            if(NWScript.getLocalInt(pc, spellUUID) == SPELL_STATUS_INTERRUPTED)
            {
                NWScript.deleteLocalInt(pc, spellUUID);
                NWScript.sendMessageToPC(pc, "Your spell has been interrupted.");
                return;
            }

            NWScript.deleteLocalInt(pc, spellUUID);
            PlayerRepository repo = new PlayerRepository();
            PlayerEntity pcEntity = repo.GetByPlayerID(pcGO.getUUID());

            if(!entity.isQueuedWeaponSkill())
            {
                ability.OnImpact(pc, target);
            }
            else
            {
                HandleQueueWeaponSkill(pc, entity, ability);
            }

            // Adjust mana only if spell cost > 0
            if(ability.ManaCost(pc, entity.getBaseManaCost()) > 0)
            {
                pcEntity.setCurrentMana(pcEntity.getCurrentMana() - ability.ManaCost(pc, entity.getBaseManaCost()));
                repo.save(pcEntity);
                NWScript.sendMessageToPC(pc, ColorToken.Custom(32,223,219) + "Mana: " + pcEntity.getCurrentMana() + " / " + pcEntity.getMaxMana());
            }

            // Mark cooldown on ability category
            ApplyCooldown(pc, cooldown, ability);

            pcGO.setIsBusy(false);
        });
    }

    private static void ApplyCooldown(NWObject pc, AbilityCooldownCategoryEntity cooldown, IAbility ability)
    {
        PlayerGO pcGO = new PlayerGO(pc);
        MagicRepository magicRepo = new MagicRepository();
        float finalCooldown = ability.CooldownTime(pc, cooldown.getBaseCooldownTime());
        int cooldownSeconds = (int)finalCooldown;
        int cooldownMillis = (int)((finalCooldown - cooldownSeconds) * 100);

        PCAbilityCooldownEntity pcCooldown = magicRepo.GetPCCooldownByID(pcGO.getUUID(), cooldown.getAbilityCooldownCategoryID());
        DateTime unlockDate = DateTime.now(DateTimeZone.UTC).plusSeconds(cooldownSeconds).plusMillis(cooldownMillis);
        pcCooldown.setDateUnlocked(unlockDate.toDate());
        magicRepo.Save(pcCooldown);
    }

    private static void CheckForSpellInterruption(final NWObject pc, final String spellUUID, final NWVector position)
    {
        NWVector currentPosition = NWScript.getPosition(pc);

        if(!currentPosition.equals(position))
        {
            PlayerGO pcGO = new PlayerGO(pc);
            NWNX_Player.StopGuiTimingBar(pc, "", -1);
            pcGO.setIsBusy(false);
            NWScript.setLocalInt(pc, spellUUID, SPELL_STATUS_INTERRUPTED);
            return;
        }

        Scheduler.delay(pc, 1000, () -> CheckForSpellInterruption(pc, spellUUID, position));
    }

    private static void HandleQueueWeaponSkill(final NWObject pc, final AbilityEntity entity, IAbility ability)
    {
        final String queueUUID = UUID.randomUUID().toString();
        NWScript.setLocalInt(pc, "ACTIVE_WEAPON_SKILL", entity.getAbilityID());
        NWScript.setLocalString(pc, "ACTIVE_WEAPON_SKILL_UUID", queueUUID);
        NWScript.sendMessageToPC(pc, "Weapon skill '" + entity.getName() + "' queued for next attack.");

        ApplyCooldown(pc, entity.getCooldown(), ability);

        // Player must attack within 30 seconds after queueing or else it wears off.
        Scheduler.delay(pc, 30000, () -> {

            if(Objects.equals(NWScript.getLocalString(pc, "ACTIVE_WEAPON_SKILL_UUID"), queueUUID))
            {
                NWScript.deleteLocalInt(pc, "ACTIVE_WEAPON_SKILL");
                NWScript.deleteLocalString(pc, "ACTIVE_WEAPON_SKILL_UUID");
                NWScript.sendMessageToPC(pc, "Your weapon skill '" + entity.getName() + "' is no longer queued.");
            }
        });
    }

    public static PCEquippedAbilityEntity EquipAbility(NWObject oPC, int slotID, int abilityID)
    {
        MagicRepository repo = new MagicRepository();
        PCEquippedAbilityEntity entity = UnequipAbility(oPC, slotID);
        AbilityEntity abilityEntity = repo.GetAbilityByID(abilityID);

        if(slotID == 1) entity.setSlot1(abilityEntity);
        else if(slotID == 2) entity.setSlot2(abilityEntity);
        else if(slotID == 3) entity.setSlot3(abilityEntity);
        else if(slotID == 4) entity.setSlot4(abilityEntity);
        else if(slotID == 5) entity.setSlot5(abilityEntity);
        else if(slotID == 6) entity.setSlot6(abilityEntity);
        else if(slotID == 7) entity.setSlot7(abilityEntity);
        else if(slotID == 8) entity.setSlot8(abilityEntity);
        else if(slotID == 9) entity.setSlot9(abilityEntity);
        else if(slotID == 10) entity.setSlot10(abilityEntity);


        IAbility ability = (IAbility) ScriptHelper.GetClassByName("Abilities." + abilityEntity.getJavaScriptName());
        NWNX_Creature.AddFeatByLevel(oPC, abilityEntity.getFeatID(), 1);

        repo.Save(entity);
        ability.OnEquip(oPC);

        return entity;
    }

    public static PCEquippedAbilityEntity UnequipAbility(NWObject oPC, int slotID)
    {
        MagicRepository repo = new MagicRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PCEquippedAbilityEntity entity = repo.GetPCEquippedAbilities(pcGO.getUUID());
        IAbility ability;
        int featID = -1;
        String scriptName = null;

        if(slotID == 1)
        {
            if(entity.getSlot1() == null) return entity;

            scriptName = entity.getSlot1().getJavaScriptName();
            featID = entity.getSlot1().getFeatID();
            entity.setSlot1(null);
        }
        else if(slotID == 2)
        {
            if(entity.getSlot2() == null) return entity;

            scriptName = entity.getSlot2().getJavaScriptName();
            featID = entity.getSlot2().getFeatID();
            entity.setSlot2(null);
        }
        else if(slotID == 3)
        {
            if(entity.getSlot3() == null) return entity;

            scriptName = entity.getSlot3().getJavaScriptName();
            featID = entity.getSlot3().getFeatID();
            entity.setSlot3(null);
        }
        else if(slotID == 4)
        {
            if(entity.getSlot4() == null) return entity;

            scriptName = entity.getSlot4().getJavaScriptName();
            featID = entity.getSlot4().getFeatID();
            entity.setSlot4(null);
        }
        else if(slotID == 5)
        {
            if(entity.getSlot5() == null) return entity;

            scriptName = entity.getSlot5().getJavaScriptName();
            featID = entity.getSlot5().getFeatID();
            entity.setSlot5(null);
        }
        else if(slotID == 6)
        {
            if(entity.getSlot6() == null) return entity;

            scriptName = entity.getSlot6().getJavaScriptName();
            featID = entity.getSlot6().getFeatID();
            entity.setSlot6(null);
        }
        else if(slotID == 7)
        {
            if(entity.getSlot7() == null) return entity;

            scriptName = entity.getSlot7().getJavaScriptName();
            featID = entity.getSlot7().getFeatID();
            entity.setSlot7(null);
        }
        else if(slotID == 8)
        {
            if(entity.getSlot8() == null) return entity;

            scriptName = entity.getSlot8().getJavaScriptName();
            featID = entity.getSlot8().getFeatID();
            entity.setSlot8(null);
        }
        else if(slotID == 9)
        {
            if(entity.getSlot9() == null) return entity;

            scriptName = entity.getSlot9().getJavaScriptName();
            featID = entity.getSlot9().getFeatID();
            entity.setSlot9(null);
        }
        else if(slotID == 10)
        {
            if(entity.getSlot10() == null) return entity;

            scriptName = entity.getSlot10().getJavaScriptName();
            featID = entity.getSlot10().getFeatID();
            entity.setSlot10(null);
        }

        ability = (IAbility) ScriptHelper.GetClassByName("Abilities." + scriptName);

        NWNX_Creature.RemoveFeat(oPC, featID);
        repo.Save(entity);
        ability.OnUnequip(oPC);

        return entity;
    }


    public static boolean IsAbilityEquipped(NWObject oPC, int abilityID)
    {
        MagicRepository repo = new MagicRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();

        PlayerEntity playerEntity = playerRepo.GetByPlayerID(pcGO.getUUID());
        if(pcGO.GetDatabaseItem() == NWObject.INVALID ||
           playerEntity == null)
            return false;

        PCEquippedAbilityEntity entity = repo.GetPCEquippedAbilities(pcGO.getUUID());

        return (entity.getSlot1() != null && entity.getSlot1().getAbilityID() == abilityID) ||
                (entity.getSlot2() != null && entity.getSlot2().getAbilityID() == abilityID) ||
                (entity.getSlot3() != null && entity.getSlot3().getAbilityID() == abilityID) ||
                (entity.getSlot4() != null && entity.getSlot4().getAbilityID() == abilityID) ||
                (entity.getSlot5() != null && entity.getSlot5().getAbilityID() == abilityID) ||
                (entity.getSlot6() != null && entity.getSlot6().getAbilityID() == abilityID) ||
                (entity.getSlot7() != null && entity.getSlot7().getAbilityID() == abilityID) ||
                (entity.getSlot8() != null && entity.getSlot8().getAbilityID() == abilityID) ||
                (entity.getSlot9() != null && entity.getSlot9().getAbilityID() == abilityID) ||
                (entity.getSlot10() != null && entity.getSlot10().getAbilityID() == abilityID);

    }

    public static void LearnAbility(NWObject oPC, NWObject oItem, int abilityID)
    {
        MagicRepository repo = new MagicRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        AbilityEntity entity = repo.GetAbilityByID(abilityID);
        boolean success = repo.AddAbilityToPC(pcGO.getUUID(), abilityID);

        if(success)
        {
            NWScript.destroyObject(oItem, 0.0f);
            NWScript.sendMessageToPC(oPC, "You learn " + entity.getName() + "!");
        }
        else
        {
            NWScript.sendMessageToPC(oPC, "You already know " + entity.getName() + ".");
        }
    }

    public static String OnModuleExamine(String existingDescription, NWObject oExaminer, NWObject oExaminedObject)
    {
        if(!NWScript.getIsPC(oExaminer)) return existingDescription;
        if(NWScript.getObjectType(oExaminedObject) != ObjectType.ITEM) return existingDescription;
        String resref = NWScript.getResRef(oExaminedObject);
        if(!resref.startsWith("ability_disc_")) return existingDescription;
        int abilityID = Integer.parseInt(resref.substring(13));

        MagicRepository repo = new MagicRepository();
        AbilityEntity entity = repo.GetAbilityByID(abilityID);

        String fullDescription = entity.getDescription() + "\n\n";
        fullDescription += "Mana Cost: " + entity.getBaseManaCost();

        return existingDescription + "\n\n" + fullDescription;
    }

    public static PlayerEntity RestoreMana(NWObject oPC, int amount, PlayerEntity entity)
    {
        entity.setCurrentMana(entity.getCurrentMana() + amount);
        if(entity.getCurrentMana() > entity.getMaxMana())
            entity.setCurrentMana(entity.getMaxMana());

        NWScript.sendMessageToPC(oPC, ColorToken.Custom(32,223,219) + "Mana: " + entity.getCurrentMana() + " / " + entity.getMaxMana());

        return entity;
    }

    public static void OnHitCastSpell(NWObject oPC)
    {
        NWObject oTarget = NWScript.getSpellTargetObject();
        int activeWeaponSkillID = NWScript.getLocalInt(oPC, "ACTIVE_WEAPON_SKILL");
        if(activeWeaponSkillID <= 0) return;

        MagicRepository magicRepo = new MagicRepository();
        AbilityEntity entity = magicRepo.GetAbilityByID(activeWeaponSkillID);
        IAbility ability = (IAbility) ScriptHelper.GetClassByName("Abilities." + entity.getJavaScriptName());

        ability.OnImpact(oPC, oTarget);

        NWScript.deleteLocalString(oPC, "ACTIVE_WEAPON_SKILL_UUID");
        NWScript.deleteLocalInt(oPC, "ACTIVE_WEAPON_SKILL");
    }

}
