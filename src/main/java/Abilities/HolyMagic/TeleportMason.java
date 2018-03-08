package Abilities.HolyMagic;

import Abilities.IAbility;
import Enumerations.AbilityType;
import GameObject.PlayerGO;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Ability;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

// Transports the caster and all party members within 5 meters to a random location in Mason City.
// Each player is transported individually and may or may not end up at the same location as other party members.
// Casting time is reduced by 1 second for every point of Holy Affinity.
// Casting time is reduced by 1 second for every 2 points of Wisdom beyond 10.
// Casting time is reduced by 1 second for every point of item bonus.
public class TeleportMason implements IAbility {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        return true;
    }

    @Override
    public String CannotCastSpellMessage() {
        return null;
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
        float castingTimeReduction = (skill) + (wisdom * 2) + itemBonus;
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
        String waypointTagBase = "TELEPORT_MASON_";
        for(NWObject member : NWScript.getFactionMembers(oPC, true))
        {
            if(Objects.equals(castedArea, NWScript.getResRef(NWScript.getArea(member))) &&
                    NWScript.getDistanceBetween(member, oPC) <= 5.0f)
            {
                int waypointID = ThreadLocalRandom.current().nextInt(1, 10);
                String waypointTag = waypointTagBase + waypointID;
                NWObject waypoint = NWScript.getWaypointByTag(waypointTag);
                final NWLocation location = NWScript.getLocation(waypoint);

                Scheduler.assign(member, () -> NWScript.actionJumpToLocation(location));
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
