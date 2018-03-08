package GameSystems;

import Conversation.ViewModels.ItemViewModel;
import Data.Repository.PlayerRepository;
import Entities.*;
import Enumerations.AbilityType;
import Enumerations.ProfessionType;
import Enumerations.QuestID;
import GameObject.ItemGO;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.LocalVariableHelper;
import Data.Repository.LootTableRepository;
import Data.Repository.SearchSiteRepository;
import Helper.MathHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SearchSystem {

    private static final String SearchSiteCopyResref = "srch_plc_copy";
    private static final String SearchSiteIDVariableName = "SearchSiteID";
    private static final String SearchSiteDCVariableName = "SearchSiteDC";
    private static final String SearchSiteLootTableVariableName = "SearchLootTable";
    private static final int ExtraSearchPerNumberLevels = 5;

    public static void OnChestClose(NWObject oChest)
    {
        NWObject[] items = NWScript.getItemsInInventory(oChest);

        for(NWObject item : items)
        {
            NWScript.destroyObject(item, 0.0f);
        }
    }

    public static void OnChestDisturbed(NWObject oChest)
    {
        NWObject oPC = NWScript.getLastDisturbed();
        if(!NWScript.getIsPC(oPC)) return;

        String pcName = NWScript.getName(oPC, false);
        NWObject oItem = NWScript.getInventoryDisturbItem();
        int disturbType = NWScript.getInventoryDisturbType();

        if(disturbType == InventoryDisturbType.ADDED)
        {
            NWScript.copyItem(oItem, oPC, true);
            NWScript.destroyObject(oItem, 0.0f);
        }
        else if(disturbType == InventoryDisturbType.REMOVED)
        {
            SaveChestInventory(oPC, oChest, false);
            String itemName = NWScript.getName(oItem, false);
            if(itemName.equals("")) itemName = "money";

            float minSearchSeconds = 1.5f;
            float maxSearchSeconds = 4.5f;
            float searchDelay = ThreadLocalRandom.current().nextFloat() * (maxSearchSeconds - minSearchSeconds) + minSearchSeconds;
            Scheduler.assign(oPC, () -> NWScript.actionPlayAnimation(Animation.LOOPING_GET_LOW, 1.0f, searchDelay));
            NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectCutsceneImmobilize(), oPC, searchDelay);

            // Notify party members in the vicinity
            NWObject[] players = NWScript.getPCs();
            for(NWObject pc : players)
            {
                if(NWScript.getDistanceBetween(oPC, pc) <= 20.0f &&
                        NWScript.getArea(pc).equals(NWScript.getArea(oPC)) &&
                        NWScript.getFactionEqual(pc, oPC))
                {
                    NWScript.sendMessageToPC(pc, pcName + " found " + itemName + ".");
                }
            }
        }
    }

    public static void OnChestOpen(NWObject oChest)
    {
        NWObject oPC = NWScript.getLastOpenedBy();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        if(NWScript.getActionMode(oPC, ActionMode.STEALTH))
            NWScript.setActionMode(oPC, ActionMode.STEALTH, false);

        PlayerGO pcGO = new PlayerGO(oPC);

        PlayerRepository playerRepo = new PlayerRepository();
        PlayerEntity playerEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        SearchSiteRepository searchRepo = new SearchSiteRepository();
        String resref = NWScript.getResRef(oChest);
        int chestID = NWScript.getLocalInt(oChest, SearchSiteIDVariableName);
        int skillRank = NWScript.getSkillRank(Skill.SEARCH, oPC, false);
        int numberOfSearches = (skillRank / ExtraSearchPerNumberLevels) + 1;
        PCSearchSiteEntity searchEntity = searchRepo.GetSearchSiteByID(chestID, pcGO.getUUID());
        DateTime timeLock = new DateTime(DateTimeZone.UTC);

        if(numberOfSearches <= 0) numberOfSearches = 1;

        if(playerEntity.getProfessionID() == ProfessionType.Cartographer)
        {
            if(ThreadLocalRandom.current().nextInt(0, 100) <= 15)
            {
                numberOfSearches++;
            }
        }

        if(searchEntity != null)
        {
            timeLock = new DateTime(searchEntity.getUnlockDateTime());
        }

        if(resref.equals(SearchSiteCopyResref))
        {
            NWScript.setUseableFlag(oChest, false);
        }

        QuestSystem.SpawnQuestItems(oChest, oPC);

        if(timeLock.isBefore(DateTime.now(DateTimeZone.UTC)) || searchEntity == null)
        {
            int dc = NWScript.getLocalInt(oChest, SearchSiteDCVariableName);

            // Good Eye ability occasionally grants +1 search attempt.
            if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.GoodEye))
            {
                if(ThreadLocalRandom.current().nextInt(0, 100) <= 10)
                {
                    numberOfSearches++;
                }
            }

            for(int search = 1; search <= numberOfSearches; search++)
            {
                RunSearchCycle(oPC, oChest, dc);
                dc += NWScript.random(3) + 1;
            }

            // Merchants occasionally find additional gold in search sites.
            if(playerEntity.getProfessionID() == ProfessionType.Merchant)
            {
                if(ThreadLocalRandom.current().nextInt(0, 100) <= 10)
                {
                    int goldAmount = ThreadLocalRandom.current().nextInt(1, 10);
                    NWScript.createItemOnObject("nw_it_gold001", oChest, goldAmount, "");
                }
            }

            SaveChestInventory(oPC, oChest, false);

            if(QuestSystem.GetPlayerQuestJournalID(oPC, QuestID.BootCampSearching) == 1)
            {
                QuestSystem.AdvanceQuestState(oPC, QuestID.BootCampSearching);
            }
        }
        else
        {
            for(PCSearchSiteItemEntity item : searchEntity.getSearchItems())
            {
                NWObject oItem = SCORCO.loadObject(item.getSearchItem(), NWScript.getLocation(oChest), oChest);

                // Prevent item duplication in containers
                if(NWScript.getHasInventory(oItem))
                {
                    NWObject[] containerItems = NWScript.getItemsInInventory(oItem);
                    for(NWObject containerItem : containerItems)
                    {
                        NWScript.destroyObject(containerItem, 0.0f);
                    }
                }
            }
        }
    }

    public static void OnChestUsed(NWObject oChest)
    {
        NWObject oPC = NWScript.getLastUsedBy();
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        NWLocation location = NWScript.getLocation(oChest);
        final NWObject oCopy = NWScript.createObject(ObjectType.PLACEABLE, SearchSiteCopyResref, location, false, "");
        NWScript.setName(oCopy, NWScript.getName(oChest, false));
        NWScript.setFacingPoint(NWScript.getPosition(oPC));

        LocalVariableHelper.CopyVariables(oChest, oCopy);

        Scheduler.assign(oPC, () -> NWScript.actionInteractObject(oCopy));
    }

    private static void SaveChestInventory(NWObject oPC, NWObject oChest, boolean resetTimeLock)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        SearchSiteRepository repo = new SearchSiteRepository();
        int chestID = NWScript.getLocalInt(oChest, SearchSiteIDVariableName);
        PCSearchSiteEntity entity = repo.GetSearchSiteByID(chestID, pcGO.getUUID());

        int lockHours = ThreadLocalRandom.current().nextInt(2, 5);
        Timestamp lockTime = new Timestamp(DateTime.now().plusHours(lockHours).getMillis());
        if(entity != null)
        {
            if(resetTimeLock)
            {
                lockTime = entity.getUnlockDateTime();
            }
            repo.Delete(entity);
        }

        entity = new PCSearchSiteEntity();
        entity.setPcID(pcGO.getUUID());
        entity.setSearchSiteID(chestID);
        entity.setUnlockDateTime(lockTime);

        NWObject[] inventory = NWScript.getItemsInInventory(oChest);
        for(NWObject item : inventory)
        {
            if(NWScript.getLocalInt(item, "QUEST_ID") <= 0)
            {
                PCSearchSiteItemEntity itemEntity = new PCSearchSiteItemEntity();
                itemEntity.setSearchItem(SCORCO.saveObject(item));
                itemEntity.setSearchSite(entity);

                entity.getSearchItems().add(itemEntity);
            }

        }

        repo.Save(entity);
    }


    private static void RunSearchCycle(NWObject oPC, NWObject oChest, int iDC)
    {
        int lootTable = NWScript.getLocalInt(oChest, SearchSiteLootTableVariableName);
        int skill = NWScript.getSkillRank(Skill.SEARCH, oPC, false);
        if(skill > 10) skill = 10;
        else if(skill < 0) skill = 0;

        int roll = NWScript.random(20) + 1;
        int combinedRoll = roll + skill;
        if(roll + skill >= iDC)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.SkillCheck() + "Search: *success*: (" + roll + " + " + skill + " = " + combinedRoll + " vs. DC: " + iDC + ")" + ColorToken.End(), oPC, false);
            ItemViewModel spawnItem = PickResultItem(lootTable);

            if(!spawnItem.getResref().equals("") && spawnItem.getQuantity() > 0)
            {
                NWObject foundItem = NWScript.createItemOnObject(spawnItem.getResref(), oChest, spawnItem.getQuantity(), "");
                int maxDurability = DurabilitySystem.GetMaxItemDurability(foundItem);
                if(maxDurability > -1)
                    DurabilitySystem.SetItemDurability(foundItem, NWScript.random(maxDurability) + 1);
            }
        }
        else
        {
            NWScript.floatingTextStringOnCreature(ColorToken.SkillCheck() + "Search: *failure*: (" + roll + " + " + skill + " = " + combinedRoll + " vs. DC: " + iDC + ")" + ColorToken.End(), oPC, false);
        }
    }


    private static ItemViewModel PickResultItem(int lootTable)
    {
        LootTableRepository repo = new LootTableRepository();
        LootTableEntity entity = repo.GetByLootTableID(lootTable);
        int weights[] = new int[entity.getLootTableItems().size()];

        for(int x = 0; x < entity.getLootTableItems().size(); x++)
        {
            weights[x] = entity.getLootTableItems().get(x).getWeight();
        }
        int randomIndex = MathHelper.GetRandomWeightedIndex(weights);

        LootTableItemEntity itemEntity = entity.getLootTableItems().get(randomIndex);
        Random rand = new Random();
        int quantity = rand.nextInt(itemEntity.getMaxQuantity()) + 1;

        ItemViewModel result = new ItemViewModel();
        result.setQuantity(quantity);
        result.setResref(itemEntity.getResref());

        return result;
    }
}
