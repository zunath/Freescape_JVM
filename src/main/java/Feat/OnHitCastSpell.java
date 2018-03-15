package Feat;

import Common.IScriptEventHandler;
import GameSystems.DurabilitySystem;
import GameSystems.AbilitySystem;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import java.util.Objects;

@SuppressWarnings("UnusedDeclaration")
public class OnHitCastSpell implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        if(!oPC.equals(NWObject.INVALID))
        {
            DurabilitySystem.OnHitCastSpell(oPC);
            AbilitySystem.OnHitCastSpell(oPC);
            SkillSystem.OnHitCastSpell(oPC);
            PerkSystem.OnHitCastSpell(oPC);
            HandleItemSpecificCastSpell(oPC);
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
