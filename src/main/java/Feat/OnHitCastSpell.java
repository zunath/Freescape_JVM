package Feat;

import Common.IScriptEventHandler;
import GameSystems.DurabilitySystem;
import GameSystems.MagicSystem;
import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import java.util.Objects;

@SuppressWarnings("UnusedDeclaration")
public class OnHitCastSpell implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oTarget) {
        if(!oTarget.equals(NWObject.INVALID))
        {
            DurabilitySystem.OnHitCastSpell(oTarget);
            MagicSystem.OnHitCastSpell(oTarget);
            HandleItemSpecificCastSpell(oTarget);
        }
    }

    private void HandleItemSpecificCastSpell(NWObject oTarget)
    {
        NWObject oSpellOrigin = NWScript.getSpellCastItem();
        // Item specific
        String javaScript = NWScript.getLocalString(oSpellOrigin, "JAVA_SCRIPT");

        if(!Objects.equals(javaScript, ""))
        {
            // Remove "Item." prefix if it exists.
            if(javaScript.startsWith("Item."))
                javaScript = javaScript.substring(5);
            ScriptHelper.RunJavaScript(oTarget, "Item." + javaScript);
        }
    }
}
