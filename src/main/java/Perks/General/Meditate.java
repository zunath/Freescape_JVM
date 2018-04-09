package Perks.General;

import Enumerations.CustomEffectType;
import Enumerations.PerkID;
import GameObject.PlayerGO;
import GameSystems.AbilitySystem;
import GameSystems.CustomEffectSystem;
import GameSystems.PerkSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWVector;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import static org.nwnx.nwnx2.jvm.NWScript.*;
import static org.nwnx.nwnx2.jvm.constants.All.VFX_IMP_HEAD_MIND;

public class Meditate implements IPerk {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        return CanMeditate(oPC);
    }

    @Override
    public String CannotCastSpellMessage() {
        return "You cannot meditate while you or a party member are in combat.";
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        return baseManaCost;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        return baseCastingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.Meditate);

        switch (perkLevel)
        {
            case 1: return 300.0f;
            case 2: return 270.0f;
            case 3:
            case 4:
                return 240.0f;
            case 5:
                return 210.0f;
            case 6:
            case 7:
                return 180.0f;
            default: return 300.0f;
        }
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        PlayerGO pcGO = new PlayerGO(oPC);
        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.Meditate);
        final NWVector position = getPosition(oPC);
        int amount;

        switch (perkLevel)
        {
            case 1:
            case 2:
            case 3:
            default:
                amount = 1;
                break;
            case 4:
            case 5:
            case 6:
                amount = 2;
                break;
            case 7:
                amount = 3;
                break;
        }
        Scheduler.assignNow(oPC, () -> actionPlayAnimation(Animation.LOOPING_MEDITATE, 1.0f, 6.1f));
        Scheduler.delay(oPC, 6000, () -> RunMeditate(oPC, position, amount));
        sendMessageToPC(oPC, "You begin to meditate...");
        pcGO.setIsBusy(true);
    }

    @Override
    public void OnPurchased(NWObject oPC, int newLevel) {

    }

    @Override
    public void OnRemoved(NWObject oPC) {

    }

    @Override
    public void OnItemEquipped(NWObject oPC, NWObject oItem) {

    }

    @Override
    public void OnItemUnequipped(NWObject oPC, NWObject oItem) {

    }

    @Override
    public boolean IsHostile() {
        return false;
    }

    private void RunMeditate(NWObject oPC, NWVector originalPosition, int amount)
    {
        NWVector position = getPosition(oPC);

        if(!position.equals(originalPosition) ||
                !CanMeditate(oPC) ||
                !getIsObjectValid(oPC))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            sendMessageToPC(oPC, "You stop meditating.");
            pcGO.setIsBusy(false);
            return;
        }

        AbilitySystem.RestoreMana(oPC, amount);
        NWEffect vfx = effectVisualEffect(VFX_IMP_HEAD_MIND, false);
        applyEffectToObject(DurationType.INSTANT, vfx, oPC, 0.0f);
        Scheduler.assignNow(oPC, () -> actionPlayAnimation(Animation.LOOPING_MEDITATE, 1.0f, 6.1f));
        Scheduler.delay(oPC, 6000, () -> RunMeditate(oPC, originalPosition, amount));
    }

    private boolean CanMeditate(NWObject oPC)
    {
        boolean canMeditate = true;

        if(getIsInCombat(oPC)) canMeditate = false;

        NWObject pcArea = getArea(oPC);
        for(NWObject member: getFactionMembers(oPC, true))
        {
            if(!getArea(member).equals(pcArea)) continue;

            if(getIsInCombat(member))
            {
                canMeditate = false;
                break;
            }
        }

        return canMeditate;
    }

}
