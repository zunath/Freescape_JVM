package GameSystems;

import Data.Repository.PlayerRepository;
import Data.Repository.SkillRepository;
import Entities.PCSkillEntity;
import Entities.PlayerEntity;
import Entities.SkillXPRequirementEntity;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class SkillSystem {

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
}
