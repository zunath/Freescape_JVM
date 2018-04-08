package GameSystems;

import Data.Repository.PlayerRepository;
import Entities.PCCorpseEntity;
import Entities.PCCorpseItemEntity;
import Data.Repository.PCCorpseRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.List;
import java.util.Objects;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class DeathSystem {
    // Resref and tag of the player corpse placeable
    private static final String CorpsePlaceableResref = "pc_corpse";

    // Message which displays on the Respawn pop up menu
    private static final String  RespawnMessage = "You have died. You can wait for another player to revive you or give up and permanently go to the death realm.";


    public static void OnModuleLoad()
    {
        PCCorpseRepository repo = new PCCorpseRepository();
        List<PCCorpseEntity> entities = repo.GetAll();

        for(PCCorpseEntity entity : entities)
        {
            NWObject area = getObjectByTag(entity.getAreaTag(), 0);
            NWVector position = new NWVector(entity.getPositionX(), entity.getPositionY(), entity.getPositionZ());
            NWLocation location = location(area, position, entity.getOrientation());
            NWObject corpse = createObject(ObjectType.PLACEABLE, CorpsePlaceableResref, location, false, "");
            setName(corpse, entity.getName());
            setDescription(corpse, entity.getName(), true);
            setLocalInt(corpse, "CORPSE_ID", entity.getPcCorpseID());

            for(PCCorpseItemEntity item : entity.getCorpseItems())
            {
                SCORCO.loadObject(item.getItem(), location, corpse);
            }
        }
    }

    public static void OnPlayerDeath()
    {
        final NWObject oPC = getLastPlayerDied();
        String corpseName = getName(oPC, false) + "'s Corpse";
        NWObject oHostileActor = getLastHostileActor(oPC);
        NWLocation location = getLocation(oPC);
        boolean hasItems = false;

        for(NWObject oMember : getFactionMembers(oHostileActor, false))
        {
            clearPersonalReputation(oPC, oMember);
        }

        popUpDeathGUIPanel(oPC, true, true, 0, RespawnMessage);

        NWObject corpse = createObject(ObjectType.PLACEABLE, CorpsePlaceableResref, location, false, "");
        PCCorpseEntity entity = new PCCorpseEntity();
        entity.setAreaTag(getTag(location.getArea()));
        entity.setName(corpseName);
        entity.setOrientation(location.getFacing());
        entity.setPositionX(location.getX());
        entity.setPositionY(location.getY());
        entity.setPositionZ(location.getZ());

        if(getGold(oPC) > 0)
        {
            Scheduler.assign(corpse, () -> takeGoldFromCreature(getGold(oPC), oPC, false));

            hasItems = true;
        }

        for(NWObject item : getItemsInInventory(oPC))
        {
            if(!getItemCursedFlag(item))
            {
                copyItem(item, corpse, true);
                destroyObject(item, 0.0f);
                hasItems = true;
            }
        }

        if(!hasItems)
        {
            destroyObject(corpse, 0.0f);
            return;
        }

        setName(corpse, corpseName);
        setDescription(corpse, corpseName, true);

        for(NWObject corpseItem : getItemsInInventory(corpse))
        {
            PCCorpseItemEntity corpseItemEntity = new PCCorpseItemEntity();
            byte[] data = SCORCO.saveObject(corpseItem);
            corpseItemEntity.setItem(data);
            corpseItemEntity.setCorpse(entity);
            entity.getCorpseItems().add(corpseItemEntity);
        }

        PCCorpseRepository repo = new PCCorpseRepository();
        repo.Save(entity);
        setLocalInt(corpse, "CORPSE_ID", entity.getPcCorpseID());
    }

    public static void OnPlayerRespawn()
    {
        NWObject oPC = getLastRespawnButtonPresser();

        int amount = getMaxHitPoints(oPC) / 2;
        applyEffectToObject(DurationType.INSTANT, effectResurrection(), oPC, 0.0f);
        applyEffectToObject(DurationType.INSTANT, effectHeal(amount), oPC, 0.0f);

        TeleportPlayerToBindPoint(oPC);
    }

    public static void OnCorpseDisturb(NWObject corpse)
    {
        NWObject oPC = getLastDisturbed();

        if(!getIsPC(oPC)) return;

        int corpseID = getLocalInt(corpse, "CORPSE_ID");
        NWObject oItem = getInventoryDisturbItem();
        int disturbType = getInventoryDisturbType();

        if(disturbType == InventoryDisturbType.ADDED)
        {
            actionGiveItem(oItem, oPC);
            floatingTextStringOnCreature("You cannot put items into corpses.", oPC, false);
        }
        else
        {
            PCCorpseRepository repo = new PCCorpseRepository();
            PCCorpseEntity entity = repo.GetByID(corpseID);

            entity.getCorpseItems().clear();
            for(NWObject corpseItem : getItemsInInventory(corpse))
            {
                PCCorpseItemEntity corpseItemEntity = new PCCorpseItemEntity();
                byte[] data = SCORCO.saveObject(corpseItem);
                corpseItemEntity.setItem(data);
                corpseItemEntity.setCorpse(entity);
                entity.getCorpseItems().add(corpseItemEntity);
            }

            repo.Save(entity);
        }
    }

    public static void OnCorpseClose(NWObject corpse)
    {
        NWObject[] items = getItemsInInventory(corpse);
        if(items.length <= 0)
        {
            int corpseID = getLocalInt(corpse, "CORPSE_ID");
            PCCorpseRepository repo = new PCCorpseRepository();
            PCCorpseEntity entity = repo.GetByID(corpseID);
            repo.Delete(entity);
            destroyObject(corpse, 0.0f);
        }
    }

    public static void BindSoul(NWObject oPC, boolean showMessage)
    {
        if(!getIsPC(oPC) || getIsDM(oPC)) return;

        PlayerRepository repo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());
        NWObject area = getArea(oPC);
        String areaTag = getTag(area);
        float facing = getFacing(oPC);
        NWVector position = getPosition(oPC);

        entity.setRespawnAreaTag(areaTag);
        entity.setRespawnLocationOrientation(facing);
        entity.setRespawnLocationX(position.getX());
        entity.setRespawnLocationY(position.getY());
        entity.setRespawnLocationZ(position.getZ());

        repo.save(entity);

        if(showMessage)
        {
            floatingTextStringOnCreature("Your soul has been bound to this location.", oPC, false);
        }
    }


    public static void TeleportPlayerToBindPoint(NWObject pc)
    {
        PlayerGO pcGO = new PlayerGO(pc);
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerEntity entity = playerRepo.GetByPlayerID(pcGO.getUUID());
        TeleportPlayerToBindPoint(pc, entity);
    }

    private static void TeleportPlayerToBindPoint(NWObject pc, PlayerEntity entity)
    {
        if(entity.getCurrentHunger() < 50)
            entity.setCurrentHunger(50);

        if(Objects.equals(entity.getRespawnAreaTag(), ""))
        {
            NWObject defaultRespawn = getWaypointByTag("DEFAULT_RESPAWN_POINT");
            final NWLocation location = getLocation(defaultRespawn);

            Scheduler.assign(pc, () -> actionJumpToLocation(location));
        }
        else {
            Scheduler.assign(pc, () -> {
                NWObject area = getObjectByTag(entity.getRespawnAreaTag(), 0);
                NWVector position = vector(entity.getRespawnLocationX(), entity.getRespawnLocationY(), entity.getRespawnLocationZ());
                NWLocation location = location(area, position, entity.getRespawnLocationOrientation());
                actionJumpToLocation(location);
            });
        }
    }
}
