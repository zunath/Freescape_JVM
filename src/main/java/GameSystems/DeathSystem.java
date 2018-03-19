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
            NWObject area = NWScript.getObjectByTag(entity.getAreaTag(), 0);
            NWVector position = new NWVector(entity.getPositionX(), entity.getPositionY(), entity.getPositionZ());
            NWLocation location = NWScript.location(area, position, entity.getOrientation());
            NWObject corpse = NWScript.createObject(ObjectType.PLACEABLE, CorpsePlaceableResref, location, false, "");
            NWScript.setName(corpse, entity.getName());
            NWScript.setDescription(corpse, entity.getName(), true);
            NWScript.setLocalInt(corpse, "CORPSE_ID", entity.getPcCorpseID());

            for(PCCorpseItemEntity item : entity.getCorpseItems())
            {
                SCORCO.loadObject(item.getItem(), location, corpse);
            }
        }
    }

    public static void OnPlayerDeath()
    {
        final NWObject oPC = NWScript.getLastPlayerDied();
        String corpseName = NWScript.getName(oPC, false) + "'s Corpse";
        NWObject oHostileActor = NWScript.getLastHostileActor(oPC);
        NWLocation location = NWScript.getLocation(oPC);
        boolean hasItems = false;

        for(NWObject oMember : NWScript.getFactionMembers(oHostileActor, false))
        {
            NWScript.clearPersonalReputation(oPC, oMember);
        }

        NWScript.popUpDeathGUIPanel(oPC, true, true, 0, RespawnMessage);

        NWObject corpse = NWScript.createObject(ObjectType.PLACEABLE, CorpsePlaceableResref, location, false, "");
        PCCorpseEntity entity = new PCCorpseEntity();
        entity.setAreaTag(NWScript.getTag(location.getArea()));
        entity.setName(corpseName);
        entity.setOrientation(location.getFacing());
        entity.setPositionX(location.getX());
        entity.setPositionY(location.getY());
        entity.setPositionZ(location.getZ());

        if(NWScript.getGold(oPC) > 0)
        {
            Scheduler.assign(corpse, () -> NWScript.takeGoldFromCreature(NWScript.getGold(oPC), oPC, false));

            hasItems = true;
        }

        for(NWObject item : NWScript.getItemsInInventory(oPC))
        {
            if(!NWScript.getItemCursedFlag(item))
            {
                NWScript.copyItem(item, corpse, true);
                NWScript.destroyObject(item, 0.0f);
                hasItems = true;
            }
        }

        if(!hasItems)
        {
            NWScript.destroyObject(corpse, 0.0f);
            return;
        }

        NWScript.setName(corpse, corpseName);
        NWScript.setDescription(corpse, corpseName, true);

        for(NWObject corpseItem : NWScript.getItemsInInventory(corpse))
        {
            PCCorpseItemEntity corpseItemEntity = new PCCorpseItemEntity();
            byte[] data = SCORCO.saveObject(corpseItem);
            corpseItemEntity.setItem(data);
            corpseItemEntity.setCorpse(entity);
            entity.getCorpseItems().add(corpseItemEntity);
        }

        PCCorpseRepository repo = new PCCorpseRepository();
        repo.Save(entity);
        NWScript.setLocalInt(corpse, "CORPSE_ID", entity.getPcCorpseID());
    }

    public static void OnPlayerRespawn()
    {
        NWObject oPC = NWScript.getLastRespawnButtonPresser();

        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectResurrection(), oPC, 0.0f);
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(1), oPC, 0.0f);

        Scheduler.assign(oPC, () -> {
            NWLocation lLocation = NWScript.getLocation(NWScript.getWaypointByTag("DEATH_WP"));
            NWScript.actionJumpToLocation(lLocation);
        });
    }

    public static void OnCorpseDisturb(NWObject corpse)
    {
        NWObject oPC = NWScript.getLastDisturbed();

        if(!NWScript.getIsPC(oPC)) return;

        int corpseID = NWScript.getLocalInt(corpse, "CORPSE_ID");
        NWObject oItem = NWScript.getInventoryDisturbItem();
        int disturbType = NWScript.getInventoryDisturbType();

        if(disturbType == InventoryDisturbType.ADDED)
        {
            NWScript.actionGiveItem(oItem, oPC);
            NWScript.floatingTextStringOnCreature("You cannot put items into corpses.", oPC, false);
        }
        else
        {
            PCCorpseRepository repo = new PCCorpseRepository();
            PCCorpseEntity entity = repo.GetByID(corpseID);

            entity.getCorpseItems().clear();
            for(NWObject corpseItem : NWScript.getItemsInInventory(corpse))
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
        NWObject[] items = NWScript.getItemsInInventory(corpse);
        if(items.length <= 0)
        {
            int corpseID = NWScript.getLocalInt(corpse, "CORPSE_ID");
            PCCorpseRepository repo = new PCCorpseRepository();
            PCCorpseEntity entity = repo.GetByID(corpseID);
            repo.Delete(entity);
            NWScript.destroyObject(corpse, 0.0f);
        }
    }

    public static void BindSoul(NWObject oPC)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerRepository repo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());
        NWObject area = NWScript.getArea(oPC);
        String areaTag = NWScript.getTag(area);
        float facing = NWScript.getFacing(oPC);
        NWVector position = NWScript.getPosition(oPC);

        entity.setRespawnAreaTag(areaTag);
        entity.setRespawnLocationOrientation(facing);
        entity.setRespawnLocationX(position.getX());
        entity.setRespawnLocationY(position.getY());
        entity.setRespawnLocationZ(position.getZ());

        repo.save(entity);

        NWScript.floatingTextStringOnCreature("Your soul has been bound to this location.", oPC, false);
    }

    public static void RespawnPlayer(NWObject oPC)
    {
        PlayerRepository repo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        final PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());

        if(entity.getRevivalStoneCount() <= 0)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You do not have enough revival stones to respawn." + ColorToken.End(), oPC, false);
            return;
        }

        entity.setRevivalStoneCount(entity.getRevivalStoneCount() - 1);

        if(entity.getCurrentHunger() < 50)
            entity.setCurrentHunger(50);

        TeleportPlayerToBindPoint(oPC, entity);
        repo.save(entity);
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
        if(Objects.equals(entity.getRespawnAreaTag(), ""))
        {
            NWObject defaultRespawn = NWScript.getWaypointByTag("DEFAULT_RESPAWN_POINT");
            final NWLocation location = NWScript.getLocation(defaultRespawn);

            Scheduler.assign(pc, () -> NWScript.actionJumpToLocation(location));
        }
        else {
            Scheduler.assign(pc, () -> {
                NWObject area = NWScript.getObjectByTag(entity.getRespawnAreaTag(), 0);
                NWVector position = NWScript.vector(entity.getRespawnLocationX(), entity.getRespawnLocationY(), entity.getRespawnLocationZ());
                NWLocation location = NWScript.location(area, position, entity.getRespawnLocationOrientation());
                NWScript.actionJumpToLocation(location);
            });
        }
    }
}
