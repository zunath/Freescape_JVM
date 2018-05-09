package Event.Area;

import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import GameSystems.DeathSystem;
import GameSystems.KeyItemSystem;
import GameSystems.LocationSystem;
import GameSystems.MigrationSystem;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.Objects;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("UnusedDeclaration")
public class OnEnter implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oArea) {
        NWObject oPC = getEnteringObject();
        MigrationSystem.OnAreaEnter(oPC);

        LocationSystem.OnAreaEnter(oArea);
        ApplySanctuaryEffects(oPC);
        AdjustCamera(oPC);

        if(getIsObjectValid(oPC) && getIsPC(oPC) && !getIsDM(oPC))
            exportSingleCharacter(oPC);
    }

    private void CheckForMovement(final NWObject oPC, final NWLocation location)
    {
        if(!getIsObjectValid(oPC) || getIsDead(oPC)) return;

        NWLocation currentLocation = getLocation(oPC);
        String areaResref = getResRef(getArea(oPC));

        if(!Objects.equals(areaResref, getResRef(location.getArea())) ||
           currentLocation.getFacing() != location.getFacing() ||
           currentLocation.getX() != location.getX() ||
           currentLocation.getY() != location.getY() ||
           currentLocation.getZ() != location.getZ())
        {
            for(NWEffect effect : getEffects(oPC))
            {
                int type = getEffectType(effect);
                if(type == EffectType.DAMAGE_REDUCTION || type == EffectType.SANCTUARY)
                {
                    removeEffect(oPC, effect);
                }
            }

            return;
        }

        Scheduler.delay(oPC, 1000, () -> CheckForMovement(oPC, location));
    }

    private void ApplySanctuaryEffects(final NWObject oPC)
    {
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;
        if(getCurrentHitPoints(oPC) <= 0) return;
        if(getTag(getArea(oPC)).equals("ooc_area")) return;

        NWEffect sanctuary = effectSanctuary(99);
        NWEffect dr = effectDamageReduction(50, DamagePowerPlus.TWENTY, 0);
        sanctuary = tagEffect(sanctuary, "AREA_ENTRY_SANCTUARY");
        dr = tagEffect(dr, "AREA_ENTRY_DAMAGE_REDUCTION");

        applyEffectToObject(DurationType.PERMANENT, sanctuary, oPC, 0.0f);
        applyEffectToObject(DurationType.PERMANENT, dr, oPC, 0.0f);
        final NWLocation location = getLocation(oPC);

        Scheduler.delay(oPC, 3500, () -> CheckForMovement(oPC, location));
    }

    private void ShowMap(NWObject oPC, NWObject oArea)
    {
        int keyItemID = getLocalInt(oArea, "MAP_ID");
        boolean areaShowsMap = getLocalInt(oArea, "SHOW_MAP") == 1;
        boolean hasKeyItem = KeyItemSystem.PlayerHasKeyItem(oPC, keyItemID);

        if(areaShowsMap || hasKeyItem)
        {
            exploreAreaForPlayer(oArea, oPC, true);
        }
    }

    private void AdjustCamera(NWObject oPC)
    {
        if(!getIsPC(oPC)) return;

        float facing = getFacing(oPC) - 180;
        setCameraFacing(facing, 1.0f, 1.0f, CameraTransitionType.SNAP);
    }

}
