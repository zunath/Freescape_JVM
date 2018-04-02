package Placeable.CraftingForge;

import Common.IScriptEventHandler;
import Entities.PCSkillEntity;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameObject.PlayerGO;
import GameSystems.FoodSystem;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class FinishSmelt implements IScriptEventHandler {
    @Override
    public void runScript(NWObject pc) {
        PlayerGO pcGO = new PlayerGO(pc);
        NWObject forge = getLocalObject(pc, "FORGE");
        String resref = getLocalString(forge, "FORGE_ORE");

        deleteLocalObject(forge, "FORGE_USER");
        deleteLocalObject(pc, "FORGE");
        deleteLocalString(forge, "FORGE_ORE");
        pcGO.setIsBusy(false);

        PCSkillEntity pcSkill = SkillSystem.GetPCSkill(pc, SkillID.Metalworking);
        int level = CraftingForgeCommon.GetIngotLevel(resref);
        String ingotResref = CraftingForgeCommon.GetIngotResref(resref);
        if(pcSkill == null || level < 0 || ingotResref.equals("")) return;

        int delta = pcSkill.getRank() - level;
        int count = 2;

        if(delta > 2) count = delta;
        if(count > 6) count = 6;

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= PerkSystem.GetPCPerkLevel(pc, PerkID.Lucky))
        {
            count++;
        }

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= PerkSystem.GetPCPerkLevel(pc, PerkID.SmeltingMastery) * 10)
        {
            count++;
        }

        for(int x = 1; x <= count; x++)
        {
            createItemOnObject(ingotResref, pc, 1, "");
        }

        int xp = (int)SkillSystem.CalculateSkillAdjustedXP(100, level, pcSkill.getRank());
        SkillSystem.GiveSkillXP(pc, SkillID.Metalworking, xp);

        FoodSystem.DecreaseHungerLevel(pc, 1);
    }
}
