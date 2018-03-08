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

    public void StripAllItemProperties()
    {
        for(NWItemProperty prop : NWScript.getItemProperties(item))
        {
            NWScript.removeItemProperty(item, prop);
        }
    }

    public boolean HasItemProperty(int itemPropertyID)
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

}
