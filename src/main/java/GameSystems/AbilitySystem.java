package GameSystems;

import Bioware.Position;
import Data.Repository.CooldownRepository;
import Data.Repository.PerkRepository;
import Data.Repository.PlayerRepository;
import Entities.CooldownCategoryEntity;
import Entities.PCCooldownEntity;
import Entities.PerkEntity;
import Entities.PlayerEntity;
import Enumerations.CustomItemType;
import Enumerations.PerkExecutionTypeID;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ScriptHelper;
import Helper.TimeHelper;
import NWNX.NWNX_Player;
import Perks.IPerk;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.NWVector;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AbilitySystem {

    private static int SPELL_STATUS_STARTED = 1;
    private static int SPELL_STATUS_INTERRUPTED = 2;

    public static void OnModuleItemActivated()
    {
        NWObject pc = NWScript.getItemActivator();
        NWObject item = NWScript.getItemActivated();
        NWObject target =  NWScript.getItemActivatedTarget();
        int perkID = NWScript.getLocalInt(item, "ACTIVATION_PERK_ID");
        if(perkID <= 0) return;

        PlayerRepository playerRepo = new PlayerRepository();
        PerkRepository perkRepo = new PerkRepository();
        CooldownRepository cdRepo = new CooldownRepository();
        PlayerGO pcGO = new PlayerGO(pc);
        PerkEntity perk = perkRepo.GetPerkByID(perkID);

        if(perk == null) return;
        IPerk perkAction = (IPerk) ScriptHelper.GetClassByName("Perks." + perk.getJavaScriptName());
        if(perkAction == null) return;

        PlayerEntity playerEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        if(PerkSystem.GetPCPerkLevel(pc, perk.getPerkID()) <= 0)
        {
            NWScript.sendMessageToPC(pc, "You do not meet the prerequisites to use this ability.");
            return;
        }

        if(perkAction.IsHostile())
        {
            if(!PVPSanctuarySystem.IsPVPAttackAllowed(pc, target)) return;
        }

        if(!Objects.equals(NWScript.getResRef(NWScript.getArea(pc)), NWScript.getResRef(NWScript.getArea(target))) ||
                NWScript.lineOfSightObject(pc, target) == 0)
        {
            NWScript.sendMessageToPC(pc, "You cannot see your target.");
            return;
        }

        if(!perkAction.CanCastSpell(pc, target))
        {
            NWScript.sendMessageToPC(pc,
                    perkAction.CannotCastSpellMessage() == null ?
                            "That ability cannot be used at this time." :
                            perkAction.CannotCastSpellMessage());
            return;
        }

        int manaCost = perkAction.ManaCost(pc, perkAction.ManaCost(pc, perk.getBaseManaCost()));
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
        PCCooldownEntity pcCooldown = cdRepo.GetPCCooldownByID(pcGO.getUUID(), perk.getCooldown().getCooldownCategoryID());
        DateTime unlockDateTime = new DateTime(pcCooldown.getDateUnlocked());
        DateTime now = DateTime.now(DateTimeZone.UTC);

        if(unlockDateTime.isAfter(now))
        {
            String timeToWait = TimeHelper.GetTimeToWaitLongIntervals(now, unlockDateTime, false);
            NWScript.sendMessageToPC(pc, "That ability can be used again in " + timeToWait + ".");
            return;
        }

        // Spells w/ casting time
        if(perk.getPerkExecutionTypeID() == PerkExecutionTypeID.Spell)
        {
            CastSpell(pc, target, perk, perkAction, perk.getCooldown());
        }
        // Combat Abilities w/o casting time
        else if(perk.getPerkExecutionTypeID() == PerkExecutionTypeID.CombatAbility)
        {
            perkAction.OnImpact(pc, target);

            if(manaCost > 0)
            {
                playerEntity.setCurrentMana(playerEntity.getCurrentMana() - manaCost);
                playerRepo.save(playerEntity);
            }
            ApplyCooldown(pc, perk.getCooldown(), perkAction);
        }
        // Queued Weapon Skills
        else if(perk.getPerkExecutionTypeID() == PerkExecutionTypeID.QueuedWeaponSkill)
        {
            HandleQueueWeaponSkill(pc, perk, perkAction);
        }
    }

    private static void CastSpell(final NWObject pc,
                                  final NWObject target,
                                  final PerkEntity entity,
                                  final IPerk perk,
                                  final CooldownCategoryEntity cooldown)
    {
        final String spellUUID = UUID.randomUUID().toString();
        final PlayerGO pcGO = new PlayerGO(pc);
        int itemBonus = pcGO.CalculateCastingSpeed();
        float baseCastingTime = perk.CastingTime(pc, entity.getBaseCastingTime());
        float castingTime = baseCastingTime;

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

        // Heavy armor increases casting time by 2x the base casting time
        NWObject armor = NWScript.getItemInSlot(InventorySlot.CHEST, pc);
        ItemGO armorGO = new ItemGO(armor);
        if(armorGO.getCustomItemType() == CustomItemType.HeavyArmor)
        {
            castingTime = baseCastingTime * 2;
        }

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

            if(entity.getPerkExecutionTypeID() == PerkExecutionTypeID.Spell ||
                    entity.getPerkExecutionTypeID() == PerkExecutionTypeID.CombatAbility)
            {
                perk.OnImpact(pc, target);
            }
            else
            {
                HandleQueueWeaponSkill(pc, entity, perk);
            }


            // Adjust mana only if spell cost > 0
            PlayerEntity pcEntity = repo.GetByPlayerID(pcGO.getUUID());
            if(perk.ManaCost(pc, entity.getBaseManaCost()) > 0)
            {
                pcEntity.setCurrentMana(pcEntity.getCurrentMana() - perk.ManaCost(pc, entity.getBaseManaCost()));
                repo.save(pcEntity);
                NWScript.sendMessageToPC(pc, ColorToken.Custom(32,223,219) + "Mana: " + pcEntity.getCurrentMana() + " / " + pcEntity.getMaxMana());
            }

            if(ThreadLocalRandom.current().nextInt(100) + 1 <= 3)
            {
                FoodSystem.DecreaseHungerLevel(pc, 1);
            }
            // Mark cooldown on category
            ApplyCooldown(pc, cooldown, perk);

            pcGO.setIsBusy(false);
        });
    }

    private static void ApplyCooldown(NWObject pc, CooldownCategoryEntity cooldown, IPerk ability)
    {
        PlayerGO pcGO = new PlayerGO(pc);
        CooldownRepository cdRepo = new CooldownRepository();
        float finalCooldown = ability.CooldownTime(pc, cooldown.getBaseCooldownTime());
        int cooldownSeconds = (int)finalCooldown;
        int cooldownMillis = (int)((finalCooldown - cooldownSeconds) * 100);

        PCCooldownEntity pcCooldown = cdRepo.GetPCCooldownByID(pcGO.getUUID(), cooldown.getCooldownCategoryID());
        DateTime unlockDate = DateTime.now(DateTimeZone.UTC).plusSeconds(cooldownSeconds).plusMillis(cooldownMillis);
        pcCooldown.setDateUnlocked(unlockDate.toDate());
        cdRepo.Save(pcCooldown);
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

    private static void HandleQueueWeaponSkill(final NWObject pc, final PerkEntity entity, IPerk ability)
    {
        final String queueUUID = UUID.randomUUID().toString();
        NWScript.setLocalInt(pc, "ACTIVE_WEAPON_SKILL", entity.getPerkID());
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

        PerkRepository magicRepo = new PerkRepository();
        PerkEntity entity = magicRepo.GetPerkByID(activeWeaponSkillID);
        IPerk perk = (IPerk) ScriptHelper.GetClassByName("Perks." + entity.getJavaScriptName());

        if(perk != null)
        {
            perk.OnImpact(oPC, oTarget);
        }

        NWScript.deleteLocalString(oPC, "ACTIVE_WEAPON_SKILL_UUID");
        NWScript.deleteLocalInt(oPC, "ACTIVE_WEAPON_SKILL");
    }

}
