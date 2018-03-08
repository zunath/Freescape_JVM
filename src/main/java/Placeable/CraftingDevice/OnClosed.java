package Placeable.CraftingDevice;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class OnClosed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject device) {
        NWObject oPC = NWScript.getLastClosedBy();

        for(NWObject item : NWScript.getItemsInInventory(device))
        {
            String resref = NWScript.getResRef(item);

            if(!resref.equals("cft_choose_bp") && !resref.equals("cft_craft_item"))
            {
                NWScript.copyItem(item, oPC, true);
            }
            NWScript.destroyObject(item, 0.0f);
        }
    }
}
