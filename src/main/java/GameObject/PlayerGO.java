package GameObject;

import Common.Constants;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import Enumerations.CustomEffectType;
import GameSystems.CustomEffectSystem;
import NWNX.NWNX_Creature;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.Inventory;

import java.util.Date;
import java.util.UUID;

public class PlayerGO {
    private NWObject _pc;

    public PlayerGO(NWObject pc)
    {
        _pc = pc;
    }

    public NWObject GetDatabaseItem()
    {
        return NWScript.getItemPossessedBy(_pc, Constants.PCDatabaseTag);
    }

    public String getUUID()
    {
        String uuid = NWScript.getLocalString(_pc, Constants.PCIDNumberVariable);

        if(NWScript.getIsDM(_pc) || NWScript.getIsDMPossessed(_pc))
        {
            if(uuid.equals(""))
            {
                uuid = UUID.randomUUID().toString();
                NWScript.setLocalString(_pc, Constants.PCIDNumberVariable, uuid);
            }
        }
        else
        {
            NWObject oDatabase = GetDatabaseItem();
            if(uuid.equals(""))
            {
                uuid = NWScript.getLocalString(oDatabase, Constants.PCIDNumberVariable);
            }
            if(uuid.equals(""))
            {
                uuid = UUID.randomUUID().toString();
            }

            NWScript.setLocalString(oDatabase, Constants.PCIDNumberVariable, uuid);
            NWScript.setLocalString(_pc, Constants.PCIDNumberVariable, uuid);
        }

        return uuid;
    }

    public PlayerEntity createEntity()
    {
        NWLocation location = NWScript.getLocation(_pc);

        PlayerEntity entity = new PlayerEntity();
        entity.setPCID(getUUID());
        entity.setCharacterName(NWScript.getName(_pc, false));
        entity.setHitPoints(NWScript.getMaxHitPoints(_pc));
        entity.setLocationAreaTag(NWScript.getTag(NWScript.getArea(_pc)));
        entity.setLocationOrientation(NWScript.getFacing(_pc));
        entity.setLocationX(location.getX());
        entity.setLocationY(location.getY());
        entity.setLocationZ(location.getZ());
        entity.setMaxHunger(150);
        entity.setCurrentHunger(150);
        entity.setCurrentHungerTick(300);
        entity.setMaxMana(0);
        entity.setCurrentMana(10);
        entity.setCurrentManaTick(20);
        entity.setHitPoints(NWScript.getMaxHitPoints(_pc));
        entity.setRegenerationTick(20);
        entity.setHpRegenerationAmount(1);
        entity.setCreateTimestamp(new Date());
        entity.setUnallocatedSP(5);
        entity.setNextSPResetDate(null);
        entity.setNumberOfSPResets(0);
        entity.setVersionNumber(Constants.PlayerVersionNumber);
        entity.setResetTokens(3);
        entity.setRevivalStoneCount(3);
        entity.setRespawnLocationOrientation(0.0f);
        entity.setRespawnAreaTag("");
        entity.setRespawnLocationX(0.0f);
        entity.setRespawnLocationY(0.0f);
        entity.setRespawnLocationZ(0.0f);
        entity.setStrBase(NWNX_Creature.GetRawAbilityScore(_pc, Ability.STRENGTH));
        entity.setDexBase(NWNX_Creature.GetRawAbilityScore(_pc, Ability.DEXTERITY));
        entity.setConBase(NWNX_Creature.GetRawAbilityScore(_pc, Ability.CONSTITUTION));
        entity.setIntBase(NWNX_Creature.GetRawAbilityScore(_pc, Ability.INTELLIGENCE));
        entity.setWisBase(NWNX_Creature.GetRawAbilityScore(_pc, Ability.WISDOM));
        entity.setChaBase(NWNX_Creature.GetRawAbilityScore(_pc, Ability.CHARISMA));


        return entity;
    }

    public void destroyAllInventoryItems(boolean destroyDatabaseItem)
    {
        for(NWObject item : NWScript.getItemsInInventory(_pc))
        {
            if(NWScript.getResRef(item).equals(Constants.PCDatabaseTag))
            {
                if(destroyDatabaseItem)
                {
                    NWScript.destroyObject(item, 0.0f);
                }
            }
            else
            {
                NWScript.destroyObject(item, 0.0f);
            }
        }
    }

    public void destroyAllEquippedItems()
    {
        NWObject oInventory = NWScript.getItemInSlot(Inventory.SLOT_ARMS, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_ARROWS, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_BELT, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_BOLTS, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_BOOTS, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_BULLETS, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_CARMOUR, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_CHEST, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_CLOAK, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_CWEAPON_B, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_CWEAPON_L, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_CWEAPON_R, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_HEAD, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_LEFTHAND, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_LEFTRING, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_NECK, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_RIGHTHAND, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
        oInventory = NWScript.getItemInSlot(Inventory.SLOT_RIGHTRING, _pc);
        NWScript.destroyObject(oInventory, 0.0f);
    }

    public void UnequipAllItems()
    {
        Scheduler.assign(_pc, () -> {
            for(int slot = 0; slot < 14; slot++)
            {
                NWScript.actionUnequipItem(NWScript.getItemInSlot(slot, _pc));
            }
        });
    }

    public void setHasPVPSanctuaryOverride(boolean value)
    {
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(getUUID());

        entity.setSanctuaryOverrideEnabled(value);
        repo.save(entity);
    }

    public boolean hasPVPSanctuary()
    {
        if(NWScript.getIsDM(_pc) || !NWScript.getIsPC(_pc) || NWScript.getIsDMPossessed(_pc)) return false;

        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(getUUID());
        
        DateTime createDate = new DateTime(entity.getCreateTimestamp());
        boolean hasOverride = entity.isSanctuaryOverrideEnabled();
        DateTime currentTime = new DateTime(DateTimeZone.UTC);

        return !(hasOverride || createDate.isAfter(currentTime));
    }

    public void setIsBusy(boolean isBusy)
    {
        NWScript.setLocalInt(_pc, "IS_BUSY", isBusy ? 1 : 0);
    }

    public boolean isBusy()
    {
        return NWScript.getLocalInt(_pc, "IS_BUSY") == 1;
    }

    public void removeEffect(int effectType)
    {
        for(NWEffect effect : NWScript.getEffects(_pc))
        {
            if(NWScript.getEffectType(effect) == effectType)
            {
                NWScript.removeEffect(_pc, effect);
            }
        }
    }

    public int CalculateCastingSpeed()
    {
        int castingSpeed = 0;

        for(int itemSlot = 0; itemSlot < Constants.NumberOfInventorySlots; itemSlot++)
        {
            NWObject item = NWScript.getItemInSlot(itemSlot, _pc);
            ItemGO itemGO = new ItemGO(item);
            castingSpeed = castingSpeed + itemGO.getCastingSpeed();
        }

        if(castingSpeed < -99)
            castingSpeed = -99;
        else if(castingSpeed > 99)
            castingSpeed = 99;

        return castingSpeed;
    }

    public int CalculateEffectAC(NWObject oPC)
    {
        int effectLevel = CustomEffectSystem.GetActiveEffectLevel(oPC, CustomEffectType.Aegis);
        int aegisAC = 0;

        if(effectLevel > 0)
        {
            switch(effectLevel)
            {
                case 1:
                    aegisAC = 1;
                    break;
                case 2:
                case 3:
                    aegisAC = 2;
                    break;
                case 4:
                    aegisAC = 3;
                    break;
                case 5:
                    aegisAC = 4;
                    break;
                default: return 0;
            }
        }

        return aegisAC;
    }

}
