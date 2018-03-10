package GameObject;

import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class ItemGO {
    private String tag;
    private String resref;
    private String script;
    private NWObject item;

    public ItemGO(NWObject oItem)
    {
        this.item = oItem;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void stripAllItemProperties()
    {
        for(NWItemProperty prop : NWScript.getItemProperties(item))
        {
            NWScript.removeItemProperty(item, prop);
        }
    }

    public boolean hasItemProperty(int itemPropertyID)
    {
        boolean hasItemProperty = false;
        for(NWItemProperty ip : NWScript.getItemProperties(item))
        {
            if(NWScript.getItemPropertyType(ip) == itemPropertyID)
            {
                hasItemProperty = true;
                break;
            }
        }

        return hasItemProperty;
    }

    public void setAC(int ac)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_AC", ac);
    }

    public int getAC()
    {
        return NWScript.getLocalInt(item, "CUSTOM_ITEM_PROPERTY_AC");
    }

    public int getCustomItemType()
    {
        return NWScript.getLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE");
    }

    public void setCustomItemType(int itemType)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE", itemType);
    }

    public int getRecommendedLevel()
    {
        return NWScript.getLocalInt(item , "CUSTOM_ITEM_PROPERTY_TYPE_RECOMMENDED_LEVEL");
    }

    public void setRecommendedLevel(int recommendedSkillLevel)
    {
        NWScript.setLocalInt(item, "CUSTOM_ITEM_PROPERTY_TYPE_RECOMMENDED_LEVEL", recommendedSkillLevel);
    }

}
