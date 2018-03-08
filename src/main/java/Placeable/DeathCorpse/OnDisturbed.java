package Placeable.DeathCorpse;

import Common.IScriptEventHandler;
import GameSystems.DeathSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("UnusedDeclaration")
public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        DeathSystem.OnCorpseDisturb(objSelf);
    }
}
