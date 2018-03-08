package Item;

import Common.IScriptEventHandler;
import GameSystems.MagicSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class AbilityDisc implements IScriptEventHandler{
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = NWScript.getItemActivated();
        String resref = NWScript.getResRef(oItem);
        int abilityID = Integer.parseInt(resref.substring(13));
        MagicSystem.LearnAbility(oPC, oItem, abilityID);
    }
}
