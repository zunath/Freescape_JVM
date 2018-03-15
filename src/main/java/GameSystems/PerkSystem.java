package GameSystems;

import Data.Repository.PerkRepository;
import Entities.PCPerksEntity;
import Entities.PerkEntity;
import Entities.PerkLevelEntity;
import Enumerations.PerkExecutionTypeID;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.ScriptHelper;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

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

}
