package GameSystems;

import Data.Repository.LootTableRepository;
import Entities.LootTableEntity;
import Entities.LootTableItemEntity;
import GameSystems.Models.ItemModel;
import Helper.MathHelper;

import java.util.concurrent.ThreadLocalRandom;

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
}
