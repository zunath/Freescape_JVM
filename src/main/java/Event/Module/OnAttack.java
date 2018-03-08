package Event.Module;

import Common.IScriptEventHandler;
import GameSystems.CombatSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("UnusedDeclaration")
public class OnAttack implements IScriptEventHandler {

    @Override
    public void runScript(final NWObject oAttacker) {
        CombatSystem system = new CombatSystem();
        system.OnModuleAttack(oAttacker);
    }
}
