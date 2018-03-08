package Item.ZombieClaws;

import Enumerations.AbilityType;
import Enumerations.CustomEffectType;
import GameSystems.*;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnusedDeclaration")
public class ZombieClaw implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oZombie) {
        NWObject oPC = NWScript.getSpellTargetObject();
        NWObject oBite = NWScript.getSpellCastItem();
        String itemTag = NWScript.getTag(oBite);
        if (!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        DiseaseSystem.RunDiseaseDCCheck(oZombie, oPC, 0, 0, 0);
        RunBleedingRoutine(oPC, oZombie);
    }

    private void RunBleedingRoutine(NWObject oPC, NWObject oZombie)
    {
        int bleedDC = 10;
        int roll = ThreadLocalRandom.current().nextInt(100);
        int conBonus = (NWScript.getAbilityScore(oPC, Ability.CONSTITUTION, false) - 10) / 2;
        roll += conBonus;

        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.SturdySkin))
        {
            roll += 3;
        }

        if(roll <= bleedDC)
        {
            CustomEffectSystem.ApplyCustomEffect(oZombie, oPC, CustomEffectType.Bleeding, 6);
        }
    }
}