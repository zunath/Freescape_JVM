package Event.Module;
import Helper.ColorToken;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("unused")
public class OnPlayerDying implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		final NWObject oPC = getLastPlayerDying();

		Scheduler.assign(oPC, () -> clearAllActions(false));

		DeathFunction(oPC, 8);
	}


	private void DeathFunction(final NWObject oPC, int nDC) {
		if (!getIsObjectValid(oPC)) return;
		int iHP = getCurrentHitPoints(oPC);

		//Player Rolls a random number between 1 and 20+ConMod
		int iRoll = 20 + getAbilityModifier(Ability.CONSTITUTION, oPC);
		iRoll = random(iRoll) + 1;

		//Sanity Check
		if (nDC > 30) nDC = 30;
		else if (nDC < 4) nDC = 4;

		NWEffect eResult;
		if (iHP <= 0)
		{
			if (iRoll >= nDC) //Stabilize
			{
				nDC -= 2;
				applyEffectToObject(DurationType.INSTANT, effectHeal(1), oPC, 0.0f);
				final int dcCopy = nDC;
				Scheduler.delay(oPC, 3000, () -> DeathFunction(oPC, dcCopy));
			}
			else  //Failed!
			{
				if (random(2) + 1 == 1) nDC++;
				eResult = effectDamage(1, DamageType.MAGICAL, DamagePower.NORMAL);

				//Death!
				if (iHP <= -9) {
					applyEffectToObject(DurationType.INSTANT, effectVisualEffect(Vfx.IMP_DEATH, false), oPC, 0.0f);
					applyEffectToObject(DurationType.INSTANT, effectDeath(false, true), oPC, 0.0f);
					return;
				} else {
					sendMessageToPC(oPC, ColorToken.Orange() + "You failed to stabilize this round." + ColorToken.End());
				}
				applyEffectToObject(DurationType.INSTANT, eResult, oPC, 0.0f);

				final int dcCopy = nDC;
				Scheduler.delay(oPC, 3000, () -> DeathFunction(oPC, dcCopy));
			}
		}
	}

}
