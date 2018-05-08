package GameSystems;

import Bioware.AddItemPropertyPolicy;
import Bioware.XP2;
import Conversation.ViewModels.PerkMenuViewModel;
import Data.Repository.PerkRepository;
import Data.Repository.PlayerRepository;
import Data.Repository.SkillRepository;
import Entities.*;
import Enumerations.PerkExecutionTypeID;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ScriptHelper;
import Perks.IPerk;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.*;

import java.sql.Timestamp;
import java.util.List;

public class PerkSystem {

    // Returns the perk level of a PC, taking into account skill requirements.
    // A PC is able to purchase perks, lose skill levels through decay, and still have the perk.
    // However, the bonuses for that perk won't apply until they meet the skill rank.
    public static int GetPCPerkLevel(NWObject oPC, int perkID)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return -1;

        PlayerGO pcGO = new PlayerGO(oPC);
        PerkRepository perkRepo = new PerkRepository();
        PerkLevelEntity perkLevel = perkRepo.GetPCSkillAdjustedPerkLevel(pcGO.getUUID(), perkID);
        return perkLevel == null ? 0 : perkLevel.getLevel();
    }

    public static void OnHitCastSpell(NWObject oPC)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oItem = NWScript.getSpellCastItem();
        NWObject oTarget = NWScript.getSpellTargetObject();
        int type = NWScript.getBaseItemType(oItem);
        PerkRepository repo = new PerkRepository();
        List<PCPerksEntity> perks = repo.GetPCPerksWithExecutionType(pcGO.getUUID());

        for(PCPerksEntity perk: perks)
        {
            if(perk.getPerk().getJavaScriptName().equals("") || perk.getPerk().getPerkExecutionTypeID() == PerkExecutionTypeID.None) continue;
            IPerk perkAction = (IPerk)ScriptHelper.GetClassByName("Perks." + perk.getPerk().getJavaScriptName());
            if(perkAction == null) continue;

            if(perk.getPerk().getPerkExecutionTypeID() == PerkExecutionTypeID.ShieldOnHit)
            {
                if(type == BaseItem.SMALLSHIELD || type == BaseItem.LARGESHIELD || type == BaseItem.TOWERSHIELD)
                {
                    perkAction.OnImpact(oPC, oItem);
                }
            }
        }
    }

    public static void OnModuleItemEquipped()
    {
        NWObject oPC = NWScript.getPCItemLastEquippedBy();
        NWObject oItem = NWScript.getPCItemLastEquipped();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;
        PlayerGO pcGO = new PlayerGO(oPC);
        PerkRepository perkRepo = new PerkRepository();
        List<PCPerksEntity> perks = perkRepo.GetPCPerksByExecutionType(pcGO.getUUID(), PerkExecutionTypeID.EquipmentBased);

        for(PCPerksEntity pcPerk: perks)
        {
            String jsName = pcPerk.getPerk().getJavaScriptName();
            if(jsName == null || jsName.equals("")) continue;

            IPerk perkAction = (IPerk)ScriptHelper.GetClassByName("Perks." + jsName);
            if(perkAction == null) continue;

            perkAction.OnItemEquipped(oPC, oItem);
        }
    }

    public static void OnModuleItemUnequipped()
    {
        NWObject oPC = NWScript.getPCItemLastUnequippedBy();
        NWObject oItem = NWScript.getPCItemLastUnequipped();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;
        PlayerGO pcGO = new PlayerGO(oPC);
        PerkRepository perkRepo = new PerkRepository();
        List<PCPerksEntity> perks = perkRepo.GetPCPerksByExecutionType(pcGO.getUUID(), PerkExecutionTypeID.EquipmentBased);

        for(PCPerksEntity pcPerk: perks)
        {
            String jsName = pcPerk.getPerk().getJavaScriptName();
            if(jsName == null || jsName.equals("")) continue;

            IPerk perkAction = (IPerk)ScriptHelper.GetClassByName("Perks." + jsName);
            if(perkAction == null) continue;

            perkAction.OnItemUnequipped(oPC, oItem);
        }
    }

    public static String OnModuleExamine(String existingDescription, NWObject examiner, NWObject examinedObject)
    {
        if(!NWScript.getIsPC(examiner)) return existingDescription;
        if(NWScript.getObjectType(examinedObject) != ObjectType.ITEM) return existingDescription;
        int perkID = NWScript.getLocalInt(examinedObject, "ACTIVATION_PERK_ID");
        if(perkID <= 0) return existingDescription;

        PerkRepository perkRepo = new PerkRepository();
        PerkEntity perk = perkRepo.GetPerkByID(perkID);
        String description = existingDescription;

        description += ColorToken.Orange() + "Name: " + ColorToken.End() + perk.getName() + "\n" +
                ColorToken.Orange() + "Description: " + ColorToken.End() + perk.getDescription() + "\n";

        switch (perk.getPerkExecutionTypeID())
        {
            case PerkExecutionTypeID.CombatAbility:
                description += ColorToken.Orange() + "Type: " + ColorToken.End() + "Combat Ability\n";
                break;
            case PerkExecutionTypeID.Spell:
                description += ColorToken.Orange() + "Type: " + ColorToken.End() + "Spell\n";
                break;
            case PerkExecutionTypeID.ShieldOnHit:
                description += ColorToken.Orange() + "Type: " + ColorToken.End() + "Reactive\n";
                break;
            case PerkExecutionTypeID.QueuedWeaponSkill:
                description += ColorToken.Orange() + "Type: " + ColorToken.End() + "Queued Attack\n";
                break;
        }

        if(perk.getBaseManaCost() > 0)
        {
            description += ColorToken.Orange() + "Base Mana Cost: " + ColorToken.End() + perk.getBaseManaCost() + "\n";
        }
        if(perk.getCooldown().getBaseCooldownTime() > 0.0f)
        {
            description += ColorToken.Orange() + "Cooldown: " + ColorToken.End() + perk.getCooldown().getBaseCooldownTime() + "s\n";
        }
        if(perk.getBaseCastingTime() > 0.0f)
        {
            description += ColorToken.Orange() + "Base Casting Time: " + ColorToken.End() + perk.getBaseCastingTime() + "s\n";
        }


        return description;
    }


    public static PerkLevelEntity FindPerkLevel(List<PerkLevelEntity> levels, int findLevel)
    {
        for(PerkLevelEntity lvl: levels)
        {
            if(lvl.getLevel() == findLevel)
            {
                return lvl;
            }
        }
        return null;
    }


    public static boolean CanPerkBeUpgraded(PerkEntity perk, PCPerksEntity pcPerk, PlayerEntity player)
    {
        SkillRepository skillRepo = new SkillRepository();
        int rank = pcPerk == null ? 0 : pcPerk.getPerkLevel();
        int maxRank = perk.getLevels().size();
        if(rank+1 > maxRank) return false;

        PerkLevelEntity level = FindPerkLevel(perk.getLevels(), rank+1);
        if(level == null) return false;

        if(player.getUnallocatedSP() < level.getPrice()) return false;

        for(PerkLevelSkillRequirementEntity req: level.getSkillRequirements())
        {
            PCSkillEntity pcSkill = skillRepo.GetPCSkillByID(player.getPCID(), req.getSkill().getSkillID());
            if(pcSkill.getRank() < req.getRequiredRank()) return false;
        }

        return true;
    }

    public static void DoPerkUpgrade(NWObject oPC, int perkID)
    {
        PerkRepository perkRepo = new PerkRepository();
        PlayerRepository playerRepo = new PlayerRepository();

        PlayerGO pcGO = new PlayerGO(oPC);
        PerkEntity perk = perkRepo.GetPerkByID(perkID);
        PCPerksEntity pcPerk = perkRepo.GetPCPerkByID(pcGO.getUUID(), perkID);
        PlayerEntity player = playerRepo.GetByPlayerID(pcGO.getUUID());

        if(CanPerkBeUpgraded(perk, pcPerk, player))
        {
            if(pcPerk == null)
            {
                pcPerk = new PCPerksEntity();
                DateTime dt = new DateTime(DateTimeZone.UTC);
                pcPerk.setAcquiredDate(new Timestamp(dt.getMillis()));
                pcPerk.setPerk(perk);
                pcPerk.setPlayerID(pcGO.getUUID());
                pcPerk.setPerkLevel(0);
            }

            PerkLevelEntity nextPerkLevel = FindPerkLevel(perk.getLevels(), pcPerk.getPerkLevel()+1);
            if(nextPerkLevel == null) return;

            pcPerk.setPerkLevel(pcPerk.getPerkLevel() + 1);
            player.setUnallocatedSP(player.getUnallocatedSP() - nextPerkLevel.getPrice());

            perkRepo.Save(pcPerk);
            playerRepo.save(player);

            // If a perk is activatable, create the item on the PC.
            // Remove any existing cast spell unique power properties and add the correct one based on the DB flag.
            if(perk.getItemResref() != null && !perk.getItemResref().equals(""))
            {
                if(!NWScript.getIsObjectValid(NWScript.getItemPossessedBy(oPC, perk.getItemResref())))
                {
                    NWObject spellItem = NWScript.createItemOnObject(perk.getItemResref(), oPC, 1, "");
                    NWScript.setItemCursedFlag(spellItem, true);
                    NWScript.setLocalInt(spellItem, "ACTIVATION_PERK_ID", perk.getPerkID());

                    for(NWItemProperty ip: NWScript.getItemProperties(spellItem))
                    {
                        int ipType = NWScript.getItemPropertyType(ip);
                        int ipSubType = NWScript.getItemPropertySubType(ip);
                        if(ipType == ItemProperty.CAST_SPELL &&
                                (ipSubType == Ip.CONST_CASTSPELL_UNIQUE_POWER ||
                                        ipSubType == Ip.CONST_CASTSPELL_UNIQUE_POWER_SELF_ONLY ||
                                        ipSubType == Ip.CONST_CASTSPELL_ACTIVATE_ITEM ))
                        {
                            NWScript.removeItemProperty(spellItem, ip);
                        }
                    }

                    NWItemProperty ip;
                    if(perk.isTargetSelfOnly()) ip = NWScript.itemPropertyCastSpell(IpConst.CASTSPELL_UNIQUE_POWER_SELF_ONLY, IpConstCastspell.NUMUSES_UNLIMITED_USE);
                    else ip = NWScript.itemPropertyCastSpell(IpConst.CASTSPELL_UNIQUE_POWER, IpConstCastspell.NUMUSES_UNLIMITED_USE);

                    XP2.IPSafeAddItemProperty(spellItem, ip, 0.0f, AddItemPropertyPolicy.ReplaceExisting, false, false);
                }

                NWScript.setName(NWScript.getItemPossessedBy(oPC, perk.getItemResref()), perk.getName() + " (Lvl. " + pcPerk.getPerkLevel() + ")");
            }

            NWScript.sendMessageToPC(oPC,ColorToken.Green() + "Perk Purchased: " + perk.getName() + " (Lvl. " + pcPerk.getPerkLevel() + ")");

            IPerk perkScript = (IPerk) ScriptHelper.GetClassByName("Perks." + perk.getJavaScriptName());
            if(perkScript == null) return;
            perkScript.OnPurchased(oPC, pcPerk.getPerkLevel());
        }
        else
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You cannot purchase the perk at this time." + ColorToken.End(), oPC, false);
        }
    }

}
