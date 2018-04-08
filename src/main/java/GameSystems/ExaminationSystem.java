package GameSystems;

import Data.Repository.PerkRepository;
import Data.Repository.PlayerRepository;
import Data.Repository.SkillRepository;
import Entities.PCPerkHeaderEntity;
import Entities.PCSkillEntity;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.List;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class ExaminationSystem {

    public static void OnModuleExamine(NWObject examiner, NWObject target)
    {
        String backupDescription = getLocalString(target, "BACKUP_DESCRIPTION");

        if(!backupDescription.equals(""))
        {
            setDescription(target, backupDescription, false);
        }

        if(!getIsDM(examiner) || !getIsPC(target) || getIsDM(target) || getIsDMPossessed(target)) return;

        backupDescription = getDescription(target, false, true);
        setLocalString(target, "BACKUP_DESCRIPTION", backupDescription);

        PlayerRepository playerRepo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(target);
        PlayerEntity playerEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        String respawnAreaName = getName(getObjectByTag(playerEntity.getRespawnAreaTag(), 0), false);

        StringBuilder description =
                new StringBuilder(ColorToken.Green() + "ID: " + ColorToken.End() + pcGO.getUUID() + "\n" +
                        ColorToken.Green() + "Character Name: " + ColorToken.End() + getName(target, false) + "\n" +
                        ColorToken.Green() + "Respawn Area: " + ColorToken.End() + respawnAreaName + "\n" +
                        ColorToken.Green() + "Skill Points: " + ColorToken.End() + playerEntity.getTotalSPAcquired() + " (Unallocated: " + playerEntity.getUnallocatedSP() + ")" + "\n" +
                        ColorToken.Green() + "Hunger: " + ColorToken.End() + playerEntity.getCurrentHunger() + " / " + playerEntity.getMaxHunger() + "\n" +
                        ColorToken.Green() + "Mana: " + ColorToken.End() + playerEntity.getCurrentMana() + " / " + playerEntity.getMaxMana() + "\n\n" +
                        ColorToken.Green() + "Skill Levels: " + ColorToken.End() + "\n\n");

        SkillRepository skillRepo = new SkillRepository();
        List<PCSkillEntity> pcSkills = skillRepo.GetAllPCSkills(pcGO.getUUID());

        for(PCSkillEntity skill: pcSkills)
        {
            description.append(skill.getSkill().getName()).append(" rank ").append(skill.getRank()).append("\n");
        }

        description.append("\n\n").append(ColorToken.Green()).append("Perks: ").append(ColorToken.End());

        PerkRepository perkRepo = new PerkRepository();
        List<PCPerkHeaderEntity> pcPerks = perkRepo.GetPCPerksForMenuHeader(pcGO.getUUID());

        for(PCPerkHeaderEntity perk: pcPerks)
        {
            description.append(perk.getName()).append(" Lvl. ").append(perk.getLevel()).append("\n");
        }

        description.append("\n\n").append(ColorToken.Green()).append("Description: \n\n").append(ColorToken.End()).append(backupDescription).append("\n");
        setDescription(target, description.toString(), false);
    }

}
