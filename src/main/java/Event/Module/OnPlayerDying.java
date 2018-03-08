package Event.Module;
import Helper.ColorToken;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

@SuppressWarnings("unused")
public class OnPlayerDying implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {

		final NWObject oPC = NWScript.getLastPlayerDying();

		Scheduler.assign(oPC, () -> {
            NWScript.clearAllActions(false);
            DeathFunction(oPC, 8);
        });
	}


	private void DeathFunction(final NWObject oPC, int nDC) {
		int iHP = NWScript.getCurrentHitPoints(oPC);
		if (!NWScript.getIsObjectValid(oPC)) return;

		//Player Rolls a random number between 1 and 20+ConMod
		int iRoll = 20 + NWScript.getAbilityModifier(Ability.CONSTITUTION, oPC);
		iRoll = NWScript.random(iRoll) + 1;

		//Sanity Check
		if (nDC > 30) nDC = 30;
		else if (nDC < 4) nDC = 4;

		NWEffect eResult;
		if (iHP <= 0) {
			if (iRoll >= nDC) //Stabilize
			{
				nDC -= 2;
				eResult = NWScript.effectHeal(1);
				//PlayVoiceChat(VOICE_CHAT_LAUGH);
			} else           //Failed!
			{
				if (NWScript.random(2) + 1 == 1) nDC++;
				eResult = NWScript.effectDamage(1, DamageType.MAGICAL, DamagePower.NORMAL);

				//Death!
				if (iHP <= -9) {
					NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(Vfx.IMP_DEATH, false), oPC, 0.0f);
					NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDeath(false, true), oPC, 0.0f);
					return;
				} else {
					NWScript.sendMessageToPC(oPC, ColorToken.Orange() + "You failed to stabilize this round." + ColorToken.End());
				}
				NWScript.applyEffectToObject(DurationType.INSTANT, eResult, oPC, 0.0f);

				final int dcCopy = nDC;
				Scheduler.delay(oPC, 3000, () -> DeathFunction(oPC, dcCopy));
			}
		}
	}

}
