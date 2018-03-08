package Bioware;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

public class XP2
{

	// ----------------------------------------------------------------------------
	// Add an item property in a safe fashion, preventing unwanted stacking
	// Parameters:
	//   oItem     - the item to add the property to
	//   ip        - the itemproperty to add
	//   fDuration - set 0.0f to add the property permanent, anything else is temporary
	//   nAddItemPropertyPolicy - How to handle existing properties. Valid values are:
	//	     X2_IP_ADDPROP_POLICY_REPLACE_EXISTING - remove any property of the same type, subtype, durationtype before adding;
	//	     X2_IP_ADDPROP_POLICY_KEEP_EXISTING - do not add if any property with same type, subtype and durationtype already exists;
	//	     X2_IP_ADDPROP_POLICY_IGNORE_EXISTING - add itemproperty in any case - Do not Use with OnHit or OnHitSpellCast props!
	//   bIgnoreDurationType  - If set to TRUE, an item property will be considered identical even if the DurationType is different. Be careful when using this
	//	                          with X2_IP_ADDPROP_POLICY_REPLACE_EXISTING, as this could lead to a temporary item property removing a permanent one
	//   bIgnoreSubType       - If set to TRUE an item property will be considered identical even if the SubType is different.
	//
	// * WARNING: This function is used all over the game. Touch it and break it and the wrath
	//	            of the gods will come down on you faster than you can saz "I didn't do it"
	// ----------------------------------------------------------------------------
	public static void IPSafeAddItemProperty(NWObject oItem, NWItemProperty ip, float fDuration, AddItemPropertyPolicy nAddItemPropertyPolicy, boolean bIgnoreDurationType, boolean bIgnoreSubType)
	{
	    int nType = NWScript.getItemPropertyType(ip);
	    int nSubType = NWScript.getItemPropertySubType(ip);
	    int nDuration;
	    // if duration is 0.0f, make the item property permanent
	    if (fDuration == 0.0f)
	    {

	        nDuration = Duration.TYPE_PERMANENT;
	    } else
	    {

	        nDuration = Duration.TYPE_TEMPORARY;
	    }

	    int nDurationCompare = nDuration;
	    if (bIgnoreDurationType)
	    {
	        nDurationCompare = -1;
	    }

	    if (nAddItemPropertyPolicy == AddItemPropertyPolicy.ReplaceExisting)
	    {

	        // remove any matching properties
	        if (bIgnoreSubType)
	        {
	            nSubType = -1;
	        }
	        IPRemoveMatchingItemProperties(oItem, nType, nDurationCompare, nSubType );
	    }
	    else if (nAddItemPropertyPolicy == AddItemPropertyPolicy.KeepExisting )
	    {
	         // do not replace existing properties
	        if(IPGetItemHasProperty(oItem, ip, nDurationCompare, bIgnoreSubType))
	        {
	          return; // item already has property, return
	        }
	    }
	    else //X2_IP_ADDPROP_POLICY_IGNORE_EXISTING
	    {

	    }

	    if (nDuration == Duration.TYPE_PERMANENT)
	    {
	        NWScript.addItemProperty(nDuration,ip, oItem, 0.0f);
	    }
	    else
	    {
	        NWScript.addItemProperty(nDuration,ip, oItem,fDuration);
	    }
	}

	

	// ----------------------------------------------------------------------------
	// Removes all itemproperties with matching nItemPropertyType and
	// nItemPropertyDuration (a DURATION_TYPE_* constant)
	// ----------------------------------------------------------------------------
	public static void IPRemoveMatchingItemProperties(NWObject oItem, int nItemPropertyType, int nItemPropertyDuration, int nItemPropertySubType)
	{
		NWItemProperty[] props = NWScript.getItemProperties(oItem);
		
		for(int current = 0; current < props.length; current++)
		{
			NWItemProperty prop = props[current];
			
			// same property type?
	        if (NWScript.getItemPropertyType(prop) == nItemPropertyType)
	        {
	            // same duration or duration ignored?
	            if (NWScript.getItemPropertyDurationType(prop) == nItemPropertyDuration || nItemPropertyDuration == -1)
	            {
	                 // same subtype or subtype ignored
	                 if  (NWScript.getItemPropertySubType(prop) == nItemPropertySubType || nItemPropertySubType == -1)
	                 {
	                      // Put a warning into the logfile if someone tries to remove a permanent ip with a temporary one!
	                      /*if (nItemPropertyDuration == DURATION_TYPE_TEMPORARY &&  GetItemPropertyDurationType(ip) == DURATION_TYPE_PERMANENT)
	                      {
	                         WriteTimestampedLogEntry("x2_inc_itemprop:: IPRemoveMatchingItemProperties() - WARNING: Permanent item property removed by temporary on "+GetTag(oItem));
	                      }
	                      */
	                      NWScript.removeItemProperty(oItem, prop);
	                 }
	            }
	        }
			
		}
	}


	public static boolean IPGetItemHasProperty(NWObject oItem, NWItemProperty ipCompareTo, int nDurationCompare, boolean bIgnoreSubType)
	{
		NWItemProperty[] props = NWScript.getItemProperties(oItem);

		for(NWItemProperty ip : props)
		{
			if ((NWScript.getItemPropertyType(ip) == NWScript.getItemPropertyType(ipCompareTo)))
			{
				//PrintString ("**Testing - S: " + IntToString(GetItemPropertySubType(ip)));
				if (NWScript.getItemPropertySubType(ip) == NWScript.getItemPropertySubType(ipCompareTo) || bIgnoreSubType)
				{
					// PrintString ("***Testing - d: " + IntToString(GetItemPropertyDurationType(ip)));
					if (NWScript.getItemPropertyDurationType(ip) == nDurationCompare || nDurationCompare == -1)
					{
						//PrintString ("***FOUND");
						return true; // if duration is not ignored and durationtypes are equal, true
					}
				}
			}
		}

		return false;
	}
	
	public static void IPRemoveAllItemProperties(NWObject oItem, int nItemPropertyDuration )
	{
		NWItemProperty[] props = NWScript.getItemProperties(oItem);
		for(int current = 0; current < props.length; current++)
		{
			NWItemProperty prop = props[current];
			
			NWScript.getItemPropertyDurationType(prop);
			if(NWScript.getItemPropertyDurationType(prop) == nItemPropertyDuration)
			{
				NWScript.removeItemProperty(oItem, prop);
			}
		}
	}


}
