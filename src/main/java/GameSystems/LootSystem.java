package GameSystems;

import Data.Repository.LootTableRepository;
import Entities.LootTableEntity;
import Entities.LootTableItemEntity;
import GameSystems.Models.ItemModel;
import Helper.MathHelper;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.createItemOnObject;
import static org.nwnx.nwnx2.jvm.NWScript.getLocalInt;

public class LootSystem {
    public static ItemModel PickRandomItemFromLootTable(int lootTableID)
    {
        if(lootTableID <= 0) return null;

        LootTableRepository repo = new LootTableRepository();
        LootTableEntity entity = repo.GetByLootTableID(lootTableID);
        if(entity.getLootTableItems().size() <= 0) return null;

        int weights[] = new int[entity.getLootTableItems().size()];

        for(int x = 0; x < entity.getLootTableItems().size(); x++)
        {
            weights[x] = entity.getLootTableItems().get(x).getWeight();
        }
        int randomIndex = MathHelper.GetRandomWeightedIndex(weights);

        LootTableItemEntity itemEntity = entity.getLootTableItems().get(randomIndex);
        int quantity = ThreadLocalRandom.current().nextInt(itemEntity.getMaxQuantity()) + 1;

        ItemModel result = new ItemModel();
        result.setQuantity(quantity);
        result.setResref(itemEntity.getResref());

        return result;
    }

    public static void OnCreatureDeath(NWObject creature)
    {
        int lootTableNumber = 1;
        int lootTableID = getLocalInt(creature, "LOOT_TABLE_ID_" + lootTableNumber);
        while(lootTableID > 0)
        {
            int chance = getLocalInt(creature, "LOOT_TABLE_CHANCE_" + lootTableNumber);
            if(chance <= 0 || chance > 100) chance = 100;

            int attempts = getLocalInt(creature, "LOOT_TABLE_ATTEMPTS_" + lootTableNumber);
            if(attempts <= 0) attempts = 1;

            for(int a = 1; a <= attempts; a++)
            {
                if(ThreadLocalRandom.current().nextInt(100) + 1 <= chance)
                {
                    ItemModel model = PickRandomItemFromLootTable(lootTableID);
                    if(model == null) continue;

                    int spawnQuantity = model.getQuantity() > 1 ? ThreadLocalRandom.current().nextInt(1, model.getQuantity()) : 1;

                    for(int x = 1; x <= spawnQuantity; x++)
                    {
                        createItemOnObject(model.getResref(), creature, 1, "");
                    }
                }
            }

            lootTableNumber++;
            lootTableID = getLocalInt(creature, "LOOT_TABLE_ID_" + lootTableNumber);
        }
    }
}
