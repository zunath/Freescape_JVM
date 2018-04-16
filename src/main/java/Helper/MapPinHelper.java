package Helper;

import GameObject.MapPinGameObject;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class MapPinHelper {

    public static int GetNumberOfMapPins(NWObject oPC)
    {
        return NWScript.getLocalInt(oPC, "NW_TOTAL_MAP_PINS");
    }

    public static MapPinGameObject GetMapPin(NWObject oPC, int index)
    {
        index++;
        MapPinGameObject mapPin = new MapPinGameObject();
        mapPin.setText(NWScript.getLocalString(oPC, "NW_MAP_PIN_NTRY_" + index));
        mapPin.setPositionX(NWScript.getLocalFloat(oPC, "NW_MAP_PIN_XPOS_" + index));
        mapPin.setPositionY(NWScript.getLocalFloat(oPC, "NW_MAP_PIN_YPOS_" + index));
        mapPin.setArea(NWScript.getLocalObject(oPC, "NW_MAP_PIN_AREA_" + index));
        mapPin.setTag(NWScript.getLocalString(oPC, "CUSTOM_NW_MAP_PIN_TAG_" + index));
        mapPin.setPC(oPC);
        mapPin.setIndex(index);

        return mapPin;
    }

    public static MapPinGameObject GetMapPin(NWObject oPC, String pinTag)
    {
        for(int index = 0; index <= GetNumberOfMapPins(oPC); index++)
        {
            String mapPinTag = NWScript.getLocalString(oPC, "CUSTOM_NWN_MAP_PIN_TAG_" + index);
            if(mapPinTag.equals(pinTag))
            {
                return GetMapPin(oPC, index);
            }
        }

        return null; // Couldn't find a map pin by that tag.
    }

    public static void SetMapPin(NWObject oPC, String text, float positionX, float positionY, NWObject area, String tag)
    {
        int numberOfMapPins = GetNumberOfMapPins(oPC);
        int storeAtIndex = -1;

        for(int index = 0; index < numberOfMapPins; index++)
        {
            MapPinGameObject mapPin = GetMapPin(oPC, index);
            if(mapPin.getText().equals(""))
            {
                storeAtIndex = index;
                break;
            }
        }

        if(storeAtIndex == -1)
        {
            numberOfMapPins++;
            storeAtIndex = numberOfMapPins-1;
        }

        storeAtIndex++;
        NWScript.setLocalString(oPC, "NW_MAP_PIN_NTRY_" + storeAtIndex, text);
        NWScript.setLocalFloat(oPC, "NW_MAP_PIN_XPOS_" + storeAtIndex, positionX);
        NWScript.setLocalFloat(oPC, "NW_MAP_PIN_YPOS_" + storeAtIndex, positionY);
        NWScript.setLocalObject(oPC, "NW_MAP_PIN_AREA_" + storeAtIndex, area);
        NWScript.setLocalInt(oPC, "NW_TOTAL_MAP_PINS", numberOfMapPins);

        if(tag != null)
        {
            NWScript.setLocalString(oPC, "CUSTOM_NW_MAP_PIN_TAG_" + storeAtIndex, tag);
        }
    }

    public static void SetMapPin(NWObject oPC, String text, float positionX, float positionY, NWObject area)
    {
        SetMapPin(oPC, text, positionX, positionY, area, null);
    }

    public static void DeleteMapPin(NWObject oPC, int index)
    {
        int numberOfPins = GetNumberOfMapPins(oPC);

        if(index > numberOfPins-1) return;
        MapPinGameObject mapPin = GetMapPin(oPC, index);

        if(mapPin != null)
        {
            NWScript.setLocalString(oPC, "NW_MAP_PIN_NTRY_" + index, "");
        }
    }

    public static void DeleteMapPin(NWObject oPC, String pinTag)
    {
        MapPinGameObject mapPin = GetMapPin(oPC, pinTag);

        if(mapPin != null)
        {
            DeleteMapPin(oPC, mapPin.getIndex());
        }
    }

    public static void AddWaypointMapPin(NWObject oPC, String waypointTag, String text, String mapPinTag)
    {
        NWObject waypoint = NWScript.getWaypointByTag(waypointTag);
        NWLocation location = NWScript.getLocation(waypoint);

        SetMapPin(oPC, text, location.getX(), location.getY(), location.getArea(), mapPinTag);
    }

}
