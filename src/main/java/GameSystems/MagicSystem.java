package GameSystems;

import Data.Repository.CooldownRepository;
import Perks.IPerk;
import Bioware.Position;
import Data.Repository.PerkRepository;
import Data.Repository.PlayerRepository;
import Entities.*;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ScriptHelper;
import Helper.TimeHelper;
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
        PerkRepository perkRepo = new PerkRepository();
        CooldownRepository cdRepo = new CooldownRepository();
        PlayerGO pcGO = new PlayerGO(pc);
        int featID = NWNX_Events.OnFeatUsed_GetFeatID();
        NWObject target =  NWNX_Events.OnFeatUsed_GetTarget();
        PerkEntity entity = perkRepo.GetPerkByFeatID(featID);

        if(entity == null) return;
        IPerk perk = (IPerk) ScriptHelper.GetClassByName("Perks." + entity.getJavaScriptName());
        if(perk == null) return;

        PlayerEntity playerEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        if(perk.IsHostile())
        {
            if(!PVPSanctuarySystem.IsPVPAttackAllowed(pc, target)) return;
        }

        if(!Objects.equals(NWScript.getResRef(NWScript.getArea(pc)), NWScript.getResRef(NWScript.getArea(target))) ||
                NWScript.lineOfSightObject(pc, target) == 0)
        {
            NWScript.sendMessageToPC(pc, "You cannot see your target.");
            return;
        }

        if(!perk.CanCastSpell(pc, target))
        {
            NWScript.sendMessageToPC(pc,
                    perk.CannotCastSpellMessage() == null ?
                            "That ability cannot be used at this time." :
                            perk.CannotCastSpellMessage());
            return;
        }

        int manaCost = perk.ManaCost(pc, perk.ManaCost(pc, entity.getBaseManaCost()));
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
        PCCooldownEntity pcCooldown = cdRepo.GetPCCooldownByID(pcGO.getUUID(), entity.getCooldown().getCooldownCategoryID());
        DateTime unlockDateTime = new DateTime(pcCooldown.getDateUnlocked());
        DateTime now = DateTime.now(DateTimeZone.UTC);

        if(unlockDateTime.isAfter(now))
        {
            String timeToWait = TimeHelper.GetTimeToWaitLongIntervals(now, unlockDateTime, false);
            NWScript.sendMessageToPC(pc, "That ability can be used again in " + timeToWait + ".");
            return;
        }


        if(perk.CastingTime(pc, entity.getBaseCastingTime()) > 0.0f)
        {
            CastSpell(pc, target, entity, perk, entity.getCooldown());
        }
        else
        {
            if(!entity.isQueuedWeaponSkill())
            {
                perk.OnImpact(pc, target);

                if(manaCost > 0)
                {
                    playerEntity.setCurrentMana(playerEntity.getCurrentMana() - manaCost);
                    playerRepo.save(playerEntity);
                }
            }
            else
            {
                HandleQueueWeaponSkill(pc, entity, perk);
            }

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
        float castingTime = perk.CastingTime(pc, entity.getBaseCastingTime());

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
                perk.OnImpact(pc, target);
            }
            else
            {
                HandleQueueWeaponSkill(pc, entity, perk);
            }

            // Adjust mana only if spell cost > 0
            if(perk.ManaCost(pc, entity.getBaseManaCost()) > 0)
            {
                pcEntity.setCurrentMana(pcEntity.getCurrentMana() - perk.ManaCost(pc, entity.getBaseManaCost()));
                repo.save(pcEntity);
                NWScript.sendMessageToPC(pc, ColorToken.Custom(32,223,219) + "Mana: " + pcEntity.getCurrentMana() + " / " + pcEntity.getMaxMana());
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

    public static String OnModuleExamine(String existingDescription, NWObject oExaminer, NWObject oExaminedObject)
    {
        if(!NWScript.getIsPC(oExaminer)) return existingDescription;
        if(NWScript.getObjectType(oExaminedObject) != ObjectType.ITEM) return existingDescription;
        String resref = NWScript.getResRef(oExaminedObject);
        if(!resref.startsWith("ability_disc_")) return existingDescription;
        int abilityID = Integer.parseInt(resref.substring(13));

        PerkRepository repo = new PerkRepository();
        PerkEntity entity = repo.GetPerkByID(abilityID);

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
