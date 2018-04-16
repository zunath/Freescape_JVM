package GameSystems;

import Data.Repository.MapPinRepository;
import Entities.PCMapPinEntity;
import GameObject.MapPinGameObject;
import GameObject.PlayerGO;
import Helper.MapPinHelper;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.List;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class MapPinSystem {

    public static void OnClientEnter()
    {
        NWObject oPC = getEnteringObject();

        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;
        if(getLocalInt(oPC, "MAP_PINS_LOADED") == 1) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        MapPinRepository repo = new MapPinRepository();
        List<PCMapPinEntity> pins = repo.GetPlayerMapPins(pcGO.getUUID());

        for(PCMapPinEntity pin: pins)
        {
            NWObject area = getObjectByTag(pin.getAreaTag(), 0);
            MapPinHelper.SetMapPin(oPC, pin.getNoteText(), pin.getPositionX(), pin.getPositionY(), area);
        }

        setLocalInt(oPC, "MAP_PINS_LOADED", 1);
    }

    public static void OnClientLeave()
    {
        NWObject oPC = getExitingObject();

        if(!getIsPC(oPC) || getIsDM(oPC) || getIsDMPossessed(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        MapPinRepository repo = new MapPinRepository();
        repo.DeletePlayerMapPins(pcGO.getUUID());

        for(int x = 0; x < MapPinHelper.GetNumberOfMapPins(oPC); x++)
        {
            MapPinGameObject mapPin = MapPinHelper.GetMapPin(oPC, x);

            if(mapPin.getText().equals("")) continue;

            PCMapPinEntity entity = new PCMapPinEntity();
            entity.setAreaTag(getTag(mapPin.getArea()));
            entity.setNoteText(mapPin.getText());
            entity.setPlayerID(pcGO.getUUID());
            entity.setPositionX(mapPin.getPositionX());
            entity.setPositionY(mapPin.getPositionY());

            repo.Save(entity);
        }
    }

}
