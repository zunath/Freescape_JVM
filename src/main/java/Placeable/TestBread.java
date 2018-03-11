package Placeable;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

public class TestBread implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastUsedBy();

        /*
        NWLocation location = NWScript.getLocation(objSelf);

        NWObject creature = NWScript.createObject(ObjectType.CREATURE, "nw_goblina", location, false, "");
        CreatureGO creatureGO = new CreatureGO(creature);

        creatureGO.setDifficultyRating(1.0f);
        creatureGO.setCreatureID(1);
        creatureGO.setXPModifier(0.0f);
        */

        NWObject oreSpawn = NWScript.getWaypointByTag("ORE_SPAWN");
        NWScript.createObject(ObjectType.PLACEABLE, "copper_vein", NWScript.getLocation(oreSpawn),false, "");
        NWObject treeSpawn = NWScript.getWaypointByTag("TREE_SPAWN");
        NWScript.createObject(ObjectType.PLACEABLE, "mithril_vein", NWScript.getLocation(treeSpawn),false, "");



        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(999), oPC, 0.0f);

    }
}
