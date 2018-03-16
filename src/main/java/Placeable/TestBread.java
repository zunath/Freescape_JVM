package Placeable;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

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

        /*
        NWObject oreSpawn = NWScript.getWaypointByTag("ORE_SPAWN");
        NWScript.createObject(ObjectType.PLACEABLE, "copper_vein", NWScript.getLocation(oreSpawn),false, "");
        NWObject treeSpawn = NWScript.getWaypointByTag("TREE_SPAWN");
        NWScript.createObject(ObjectType.PLACEABLE, "mithril_vein", NWScript.getLocation(treeSpawn),false, "");
        */


        //NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(1, DamageType.MAGICAL, DamagePower.NORMAL), oPC, 0.0f);
        //NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(999), oPC, 0.0f);

        //NWScript.createItemOnObject("elm_wood", oPC, 1, "");
        //NWScript.createItemOnObject("nails", oPC, 1, "");

/*
        StructureRepository repo = new StructureRepository();
        int id = 125;
        NWObject area = NWScript.getObjectByTag("struc_importer", 0);
        for(NWObject obj: NWScript.getObjectsInArea(area))
        {
            if(NWScript.getObjectType(obj) != ObjectType.PLACEABLE) continue;

            String customname = NWScript.getLocalString(obj, "NAME");
            String name = customname.equals("") ? NWScript.getName(obj, false) : customname;

            StructureBlueprintEntity bp = new StructureBlueprintEntity();
            bp.setStructureBlueprintID(id);
            bp.setStructureCategoryID(51);
            bp.setName(name);
            bp.setDescription("");
            bp.setResref(NWScript.getResRef(obj));
            bp.setIsActive(true);
            bp.setIsTerritoryFlag(false);
            bp.setIsUseable(false);
            bp.setItemStorageCount(0);
            bp.setMaxStructuresCount(0);
            bp.setMaxBuildDistance(0.0f);
            bp.setResearchSlots(0);
            bp.setRpPerSecond(0);
            bp.setLevel(0);

            repo.Save(bp);

            id++;
        }


*/
    }
}
