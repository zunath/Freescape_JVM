package GameSystems;

import Data.Repository.PerkRepository;
import Entities.PCPerksEntity;
import Entities.PerkLevelEntity;
import Enumerations.PerkExecutionTypeID;
import GameObject.PlayerGO;
import Helper.ScriptHelper;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;

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
}
