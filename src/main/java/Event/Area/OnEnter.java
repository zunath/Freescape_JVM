package Event.Area;

import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import GameSystems.KeyItemSystem;
import GameSystems.MigrationSystem;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

import java.util.Objects;

@SuppressWarnings("UnusedDeclaration")
public class OnEnter implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oArea) {
        NWObject oPC = NWScript.getEnteringObject();
        MigrationSystem.OnAreaEnter(oPC);

        LoadLocation(oPC, oArea);
        SaveLocation(oPC, oArea);
        ApplySanctuaryEffects(oPC);
        AdjustCamera(oPC);

        if(NWScript.getIsObjectValid(oPC) && NWScript.getIsPC(oPC) && !NWScript.getIsDM(oPC))
            NWScript.exportSingleCharacter(oPC);
    }

    private void CheckForMovement(final NWObject oPC, final NWLocation location)
    {
        if(!NWScript.getIsObjectValid(oPC) || NWScript.getIsDead(oPC)) return;

        NWLocation currentLocation = NWScript.getLocation(oPC);
        String areaResref = NWScript.getResRef(NWScript.getArea(oPC));

        if(!Objects.equals(areaResref, NWScript.getResRef(location.getArea())) ||
           currentLocation.getFacing() != location.getFacing() ||
           currentLocation.getX() != location.getX() ||
           currentLocation.getY() != location.getY() ||
           currentLocation.getZ() != location.getZ())
        {
            for(NWEffect effect : NWScript.getEffects(oPC))
            {
                int type = NWScript.getEffectType(effect);
                if(type == EffectType.DAMAGE_REDUCTION || type == EffectType.SANCTUARY)
                {
                    NWScript.removeEffect(oPC, effect);
                }
            }

            return;
        }

        Scheduler.delay(oPC, 1000, () -> CheckForMovement(oPC, location));
    }

    private void ApplySanctuaryEffects(final NWObject oPC)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;
        if(NWScript.getCurrentHitPoints(oPC) <= 0) return;

        NWScript.applyEffectToObject(DurationType.PERMANENT, NWScript.effectSanctuary(99), oPC, 0.0f);
        NWScript.applyEffectToObject(DurationType.PERMANENT, NWScript.effectDamageReduction(50, DamagePowerPlus.TWENTY, 0), oPC, 0.0f);
        final NWLocation location = NWScript.getLocation(oPC);

        Scheduler.delay(oPC, 3500, () -> CheckForMovement(oPC, location));
    }

    private void SaveLocation(NWObject oPC, NWObject oArea)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;

        String sTag = NWScript.getTag(oArea);
        if(!sTag.equals("ooc_area"))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            NWLocation location = NWScript.getLocation(oPC);
            PlayerRepository repo = new PlayerRepository();
            PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());
            entity.setLocationAreaTag(sTag);
            entity.setLocationX(location.getX());
            entity.setLocationY(location.getY());
            entity.setLocationZ(location.getZ());
            entity.setLocationOrientation(NWScript.getFacing(oPC));

            repo.save(entity);
        }
    }

    private void LoadLocation(NWObject oPC, NWObject oArea)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC)) return;

        if(NWScript.getTag(oArea).equals("ooc_area"))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            PlayerEntity entity = new PlayerRepository().GetByPlayerID(pcGO.getUUID());
            NWObject area = NWScript.getObjectByTag(entity.getLocationAreaTag(), 0);
            final NWLocation location = new NWLocation(area,
                    entity.getLocationX(),
                    entity.getLocationY(),
                    entity.getLocationZ(),
                    entity.getLocationOrientation());

            Scheduler.assign(oPC, () -> NWScript.actionJumpToLocation(location));
        }
    }

    private void ShowMap(NWObject oPC, NWObject oArea)
    {
        int keyItemID = NWScript.getLocalInt(oArea, "MAP_ID");
        boolean areaShowsMap = NWScript.getLocalInt(oArea, "SHOW_MAP") == 1;
        boolean hasKeyItem = KeyItemSystem.PlayerHasKeyItem(oPC, keyItemID);

        if(areaShowsMap || hasKeyItem)
        {
            NWScript.exploreAreaForPlayer(oArea, oPC, true);
        }
    }

    private void AdjustCamera(NWObject oPC)
    {
        if(!NWScript.getIsPC(oPC)) return;

        float facing = NWScript.getFacing(oPC) - 180;
        NWScript.setCameraFacing(facing, 1.0f, 1.0f, CameraTransitionType.SNAP);
    }

}
