package Abilities.HolyMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameObject.PlayerGO;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Ability;

import java.util.Objects;

// Transports caster and all party members within 5 meters to the escape point for the area.
// Casting time is reduced by 1 second for every point of holy affinity skill.
// Casting time is reduced by 1 second for every point of item bonus.
// Casting time is reduced by 1 second for every 2 points of Wisdom beyond 10.

public class Escape implements IAbility {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        NWObject oArea = NWScript.getArea(oPC);
        String escapePoint = NWScript.getLocalString(oArea, "ESCAPE_POINT");
        return !Objects.equals(escapePoint, "");
    }

    @Override
    public String CannotCastSpellMessage() {
        return "Escape cannot be cast in this area.";
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.TouchedByHoly))
            baseManaCost--;

        return baseManaCost;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        PlayerGO pcGO = new PlayerGO(oPC);
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_HOLY_AFFINITY);
        int wisdom = NWScript.getAbilityScore(oPC, Ability.WISDOM, false) - 10;
        int itemBonus = pcGO.CalculateHolyBonus();
        float castingTimeReduction = (skill + itemBonus) + (wisdom * 2);
        float castingTime = baseCastingTime - castingTimeReduction;

        if(castingTime < 6.0f)
            castingTime = 6.0f;

        return castingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }


    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        NWObject oArea = NWScript.getArea(oTarget);
        String castedArea = NWScript.getResRef(oArea);
        String waypointTag = NWScript.getLocalString(oArea, "ESCAPE_POINT");
        final NWLocation location = NWScript.getLocation(NWScript.getWaypointByTag(waypointTag));

        NWObject[] members = NWScript.getFactionMembers(oTarget, true);

        for(NWObject member : members)
        {
            if(Objects.equals(castedArea, NWScript.getResRef(NWScript.getArea(member))) &&
                    NWScript.getDistanceBetween(member, oPC) <= 5.0f)
            {
                final NWObject memberFinal = member;
                Scheduler.delay(memberFinal, 2000, () -> Scheduler.assign(memberFinal, () -> NWScript.actionJumpToLocation(location)));
            }
        }

    }

    @Override
    public void OnEquip(NWObject oPC) {

    }

    @Override
    public void OnUnequip(NWObject oPC) {

    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
