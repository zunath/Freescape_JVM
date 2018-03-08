package Creature.Zombie;

import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import GameSystems.ProgressionSystem;
import GameSystems.QuestSystem;
import GameSystems.SpawnSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class Zombie_OnDeath implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        SpawnSystem spawnSystem = new SpawnSystem();

        NWScript.executeScript("nw_c2_default7", objSelf);
        NWScript.setIsDestroyable(false, true, false);
        NWScript.executeScript("gb_loot_corpse", objSelf);
        IncrementKillCount();
        GiveExperienceToPCParty(objSelf);

        spawnSystem.OnCreatureDeath(objSelf);
        QuestSystem.OnCreatureDeath(objSelf);
    }

    private void IncrementKillCount()
    {
        NWObject oPC = NWScript.getLastKiller();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());

        if(entity != null)
        {
            entity.setZombieKillCount(entity.getZombieKillCount() + 1);
            repo.save(entity);
        }
    }

    private void GiveExperienceToPCParty(NWObject creature)
    {
        NWObject oPC = NWScript.getLastKiller();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        String creatureAreaResref = NWScript.getResRef(NWScript.getArea(creature));
        NWObject[] partyMembers = NWScript.getFactionMembers(oPC, true);
        int difficultyRating = NWScript.getLocalInt(creature, "DIFFICULTY_RATING"); // This value is set in the SpawnSystem.java class
        int maxLevelToGainExp = difficultyRating * 10;

        for(NWObject member : partyMembers)
        {
            String areaResref = NWScript.getResRef(NWScript.getArea(member));
            float distance = NWScript.getDistanceBetween(member, creature);
            if(distance <= 20.0f && Objects.equals(areaResref, creatureAreaResref))
            {
                int level = ProgressionSystem.GetPlayerLevel(member);

                if(level <= maxLevelToGainExp)
                {
                    int exp = ThreadLocalRandom.current().nextInt(25, 50);
                    ProgressionSystem.GiveExperienceToPC(member, exp);
                }
            }

        }


    }

}
