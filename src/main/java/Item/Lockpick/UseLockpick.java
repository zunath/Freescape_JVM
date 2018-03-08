package Item.Lockpick;

import Bioware.Position;
import Enumerations.AbilityType;
import GameSystems.MagicSystem;
import Helper.ColorToken;
import Common.IScriptEventHandler;
import NWNX.NWNX_Events;
import GameSystems.ProgressionSystem;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnusedDeclaration")
public class UseLockpick implements IScriptEventHandler {

    private final String CurrentStatusVariable = "LOCKPICK_TEMPORARY_CURRENTLY_PICKING_LOCK";
    private final String UnlockingObjectVariable = "LOCKPICK_TEMP_UNLOCKING_OBJECT";
    private final String SkillRequiredVariable = "LOCKPICK_SKILL_REQUIRED";
    private final float MaxDistance = 2.5f;


    @Override
    public void runScript(final NWObject oPC) {

        NWObject oTarget = NWNX_Events.OnItemUsed_GetTarget();

        int iSkillRequired = NWScript.getLocalInt(oTarget, SkillRequiredVariable);
        int iSkill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_LOCKPICKING);

        // Already picking the lock - cancel the action and inform the user
        if(NWScript.getLocalInt(oPC, CurrentStatusVariable) == 1)
        {
            NWScript.deleteLocalInt(oPC, CurrentStatusVariable);
            NWNX_Player.StopGuiTimingBar(oPC, "", -1);
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "Lockpicking canceled!" + ColorToken.End(), oPC, false);
            NWScript.setCommandable(true, oPC);
            // Remove temporary variable linking to the NWObject being unlocked
            NWScript.deleteLocalObject(oPC, UnlockingObjectVariable);
        }
        // Object is not locked.
        else if(!NWScript.getLocked(oTarget))
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "That object is not locked." + ColorToken.End(), oPC, false);
        }
        // Object cannot be unlocked using a lockpick
        else if(iSkillRequired <= 0)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You cannot pick that lock." + ColorToken.End(), oPC, false);
        }
        // Skill level is too low
        else if(iSkillRequired > iSkill)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "Your skill level is too low. (Required: " + iSkillRequired + ")" + ColorToken.End(), oPC, false);
        }
        // Too far away
        else if(NWScript.getDistanceBetween(oPC, oTarget) > MaxDistance)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You must get closer to do that." + ColorToken.End(), oPC, false);
        }
        // All requirements met. Begin process to unlock object
        else
        {
            int iSeconds = 10 + NWScript.random(5);
            float fSeconds = iSeconds;
            int iSkillBonus = Math.abs(iSkillRequired - iSkill);

            // Number of seconds is reduced depending on how many skill ranks a player
            // has over the minimum skill required.
            // Cap is 75% of normal
            if(iSkillBonus > 0)
            {
                float fMultiplier = iSkillBonus * 0.05f;

                // Master of Unlocking ability grants +0.25 multiplier
                if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.MasterOfUnlocking))
                {
                    fMultiplier += 0.25f;
                }

                if(fMultiplier > 0.75f)
                    fMultiplier = 0.75f;

                fSeconds = fSeconds * fMultiplier;

                // Quick Pick occasionally grants near-instant unlock.
                if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.QuickPick))
                {
                    if(ThreadLocalRandom.current().nextInt(1, 100) <= 5)
                    {
                        fSeconds = 1.0f;
                    }
                }

                iSeconds = (int)fSeconds;
            }


            // Animation stuff
            Position.TurnToFaceObject(oTarget, oPC);

            final float fSecondsCopy = fSeconds;
            Scheduler.assign(oPC, () -> {
                NWScript.playAnimation(Animation.LOOPING_GET_MID, 1.0f, fSecondsCopy);
                NWScript.setCommandable(false, oPC);
            });

            Scheduler.delay(oPC, (int) (fSeconds * 1000), () -> NWScript.setCommandable(true, oPC));

            // Show timing bar, set PC's current action status, and inform PC they're picking a lock.
            NWNX_Player.StartGuiTimingBar(oPC, iSeconds, "Item.Lockpick.PerformLockpick");
            NWScript.setLocalObject(oPC, UnlockingObjectVariable, oTarget);
            NWScript.setLocalInt(oPC, CurrentStatusVariable, 1);

            Scheduler.delay(oPC, (int)(1000 * (fSeconds + 0.2)), () -> NWScript.deleteLocalInt(oPC, CurrentStatusVariable));

            NWScript.floatingTextStringOnCreature(ColorToken.Purple() + "You begin picking the lock..." + ColorToken.End(), oPC, false);
        }

    }
}
