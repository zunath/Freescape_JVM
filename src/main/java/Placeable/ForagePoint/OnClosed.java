package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import Data.Repository.SkillRepository;
import Entities.PCSkillEntity;
import Enumerations.SkillID;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnClosed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject point) {
        NWObject oPC = getLastClosedBy();

        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        SkillRepository skillRepo = new SkillRepository();
        PCSkillEntity pcSkill = skillRepo.GetPCSkillByID(pcGO.getUUID(), SkillID.Forage);
        int level = getLocalInt(point, "FORAGE_POINT_LEVEL");
        int rank = pcSkill.getRank();
        int delta = level - rank;

        // If the delta is greater than 8, we assume the player wasn't able to forage. Simply close it - don't destroy anything.
        if(delta > 8) return;

        NWObject[] items = getItemsInInventory(point);

        if(items.length <= 0)
        {
            destroyObject(point, 0.0f);
        }
    }
}
