package Creature.Zombie;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class Zombie_OnConversation implements IScriptEventHandler {

    @Override
    public void runScript(NWObject objSelf) {
        NWScript.executeScript("nw_c2_default4", objSelf);
    }
}
