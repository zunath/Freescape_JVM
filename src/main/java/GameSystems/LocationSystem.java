package GameSystems;

import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class LocationSystem {

    public static void OnAreaEnter(NWObject oArea)
    {
        NWObject oPC = getEnteringObject();

        LoadLocation(oPC, oArea);
        SaveLocation(oPC, oArea);
    }


    public static void SaveLocation(NWObject oPC, NWObject oArea)
    {
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        String sTag = getTag(oArea);
        boolean isPlayerHome = getLocalInt(oArea, "IS_PLAYER_HOME") == 1;

        if(!sTag.equals("ooc_area") && !isPlayerHome)
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            NWLocation location = getLocation(oPC);
            PlayerRepository repo = new PlayerRepository();
            PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());
            entity.setLocationAreaTag(sTag);
            entity.setLocationX(location.getX());
            entity.setLocationY(location.getY());
            entity.setLocationZ(location.getZ());
            entity.setLocationOrientation(getFacing(oPC));

            repo.save(entity);

            if(entity.getRespawnAreaTag().equals(""))
            {
                DeathSystem.BindSoul(oPC, false);
            }
        }
    }

    private static void LoadLocation(NWObject oPC, NWObject oArea)
    {
        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        if(getTag(oArea).equals("ooc_area"))
        {
            PlayerGO pcGO = new PlayerGO(oPC);
            PlayerEntity entity = new PlayerRepository().GetByPlayerID(pcGO.getUUID());
            NWObject area = getObjectByTag(entity.getLocationAreaTag(), 0);
            final NWLocation location = new NWLocation(area,
                    entity.getLocationX(),
                    entity.getLocationY(),
                    entity.getLocationZ(),
                    entity.getLocationOrientation());

            Scheduler.assign(oPC, () -> actionJumpToLocation(location));
        }
    }


}
