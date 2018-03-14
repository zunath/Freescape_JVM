package GameSystems;

import Data.Repository.PerkRepository;
import Entities.PerkLevelEntity;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

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
}
