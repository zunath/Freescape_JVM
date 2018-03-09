package Placeable;

import Common.IScriptEventHandler;
import Enumerations.SkillID;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class TestBread implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastUsedBy();

        SkillSystem.GiveSkillXP(oPC, SkillID.OneHanded, 50);
    }
}
