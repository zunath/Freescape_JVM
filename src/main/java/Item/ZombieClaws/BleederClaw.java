package Item.ZombieClaws;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import Enumerations.CustomEffectType;
import GameSystems.CustomEffectSystem;
import GameSystems.MagicSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnusedDeclaration")
public class BleederClaw implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oZombie) {
        NWObject oPC = NWScript.getSpellTargetObject();
        NWObject oBite = NWScript.getSpellCastItem();
        String itemTag = NWScript.getTag(oBite);
        if (!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        RunBleedingRoutine(oPC, oZombie);
    }

    private void RunBleedingRoutine(NWObject oPC, NWObject oZombie)
    {
        int bleedDC = 60;
        int roll = ThreadLocalRandom.current().nextInt(100);
        int conBonus = (NWScript.getAbilityScore(oPC, Ability.CONSTITUTION, false) - 10) / 2;
        roll += (conBonus * 2);

        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.SturdySkin))
        {
            roll += 20;
        }

        if(roll <= bleedDC)
        {
            int ticks = ThreadLocalRandom.current().nextInt(3, 12);
            CustomEffectSystem.ApplyCustomEffect(oZombie, oPC, CustomEffectType.Bleeding, ticks);
        }
    }
}