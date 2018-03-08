package GameSystems;

import Entities.KeyItemEntity;
import Entities.PCKeyItemEntity;
import GameObject.PlayerGO;
import Data.Repository.KeyItemRepository;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class KeyItemSystem {

    public static boolean PlayerHasKeyItem(NWObject oPC, int keyItemID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        KeyItemRepository repo = new KeyItemRepository();
        PCKeyItemEntity entity = repo.GetPCKeyItemByKeyItemID(pcGO.getUUID(), keyItemID);
        return entity != null;
    }


    public static void GivePlayerKeyItem(NWObject oPC, int keyItemID)
    {
        if(!PlayerHasKeyItem(oPC, keyItemID))
        {
            PlayerGO pcGO = new PlayerGO(oPC);

            KeyItemRepository repo = new KeyItemRepository();
            PCKeyItemEntity entity = new PCKeyItemEntity();
            entity.setPlayerID(pcGO.getUUID());
            entity.setKeyItemID(keyItemID);
            repo.Save(entity);

            KeyItemEntity keyItem = repo.GetKeyItemByID(keyItemID);

            NWScript.sendMessageToPC(oPC, "You acquired the key item '" + keyItem.getName() + "'.");
        }
    }

    public static void RemovePlayerKeyItem(NWObject oPC, int keyItemID)
    {
        if(PlayerHasKeyItem(oPC, keyItemID))
        {
            PlayerGO pcGO = new PlayerGO(oPC);

            KeyItemRepository repo = new KeyItemRepository();
            PCKeyItemEntity entity = repo.GetPCKeyItemByKeyItemID(pcGO.getUUID(), keyItemID);
            repo.Delete(entity);
        }
    }

    public static void OnModuleItemAcquired()
    {
        NWObject oPC = NWScript.getModuleItemAcquiredBy();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        NWObject oItem = NWScript.getModuleItemAcquired();
        int keyItemID = NWScript.getLocalInt(oItem, "KEY_ITEM_ID");

        if(keyItemID <= 0) return;

        GivePlayerKeyItem(oPC, keyItemID);
        NWScript.destroyObject(oItem, 0.0f);

    }


}
