package GameSystems;

import Data.Repository.FameRepository;
import Data.Repository.KeyItemRepository;
import Data.Repository.QuestRepository;
import Dialog.DialogManager;
import Entities.*;
import Enumerations.QuestType;
import GameObject.PlayerGO;
import GameSystems.Models.ItemModel;
import Helper.ColorToken;
import Helper.MapPinHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturb;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestSystem {

    private final static String TempStoragePlaceableTag = "QUEST_BARREL";
    private final static String SubmitQuestItemResref = "qst_submit";

    public static void OnClientEnter()
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();
        List<PCQuestStatusEntity> pcQuests = questRepo.GetAllPCQuestStatusesByID(pcGO.getUUID());

        for(PCQuestStatusEntity quest : pcQuests)
        {
            NWScript.addJournalQuestEntry(quest.getQuest().getJournalTag(), quest.getCurrentQuestState().getJournalStateID(), oPC, false, false, false);
        }
    }

    public static void OnItemAcquired()
    {
        NWObject oPC = NWScript.getModuleItemAcquiredBy();
        NWObject oItem = NWScript.getModuleItemAcquired();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        int questID = NWScript.getLocalInt(oItem, "QUEST_ID");

        if(questID <= 0) return;

        NWScript.setItemCursedFlag(oItem, true);
    }

    public static void AcceptQuest(NWObject oPC, int questID)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        String uuid = pcGO.getUUID();
        QuestRepository questRepo = new QuestRepository();
        FameRepository fameRepo = new FameRepository();

        PCQuestStatusEntity status = questRepo.GetPCQuestStatusByID(uuid, questID);

        if(status != null)
        {
            if(status.getCompletionDate() != null)
            {
                NWScript.sendMessageToPC(oPC, "You have already completed this quest.");
                return;
            }
            else
            {
                NWScript.sendMessageToPC(oPC, "You have already accepted this quest.");
                return;
            }
        }


        QuestEntity quest = questRepo.GetQuestByID(questID);

        if(!DoesPlayerMeetPrerequisites(oPC, quest.getPrerequisiteQuests()))
        {
            NWScript.sendMessageToPC(oPC, "You do not meet the prerequisites necessary to accept this quest.");
            return;
        }

        if(!DoesPlayerHaveRequiredKeyItems(oPC, quest.getQuestStates().get(0).getRequiredKeyItems()))
        {
            NWScript.sendMessageToPC(oPC, "You do not have the required key items to accept this quest.");
            return;
        }

        PCRegionalFameEntity fame = fameRepo.GetPCFameByID(uuid, quest.getFameRegion().getFameRegionID());

        if(fame.getAmount() < quest.getRequiredFameAmount())
        {
            NWScript.sendMessageToPC(oPC, "You do not have enough fame to accept this quest.");
            return;
        }

        status = new PCQuestStatusEntity();
        for(QuestStateEntity state : quest.getQuestStates())
        {
            if(state.getSequence() == 1)
            {
                status.setCurrentQuestState(state);
                break;
            }
        }

        if(status.getCurrentQuestState() == null)
        {
            NWScript.sendMessageToPC(oPC, "There was an error accepting the quest '" + quest.getName() + "'. Please inform an admin this quest is bugged. (QuestID: " + questID + ")");
            return;
        }

        // Give temporary key item at start of quest.
        if(quest.getStartKeyItem() != null)
        {
            KeyItemSystem.GivePlayerKeyItem(oPC, quest.getStartKeyItem().getKeyItemID());
        }

        if(!quest.getMapNoteTag().equals(""))
        {
            MapPinHelper.AddWaypointMapPin(oPC, quest.getMapNoteTag(), quest.getName(), "QST_MAP_NOTE_" + questID);
        }

        status.setQuest(quest);
        status.setPlayerID(pcGO.getUUID());
        status.setQuest(quest);

        questRepo.Save(status);

        CreateExtendedQuestDataEntries(status, questID, 1);

        NWScript.addJournalQuestEntry(quest.getJournalTag(), 1, oPC, false, false, false);
        NWScript.sendMessageToPC(oPC, "Quest '" + quest.getName() + "' accepted. Refer to your journal for more information on this quest.");
    }

    public static void AdvanceQuestState(NWObject oPC, int questID)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository repo = new QuestRepository();
        PCQuestStatusEntity questStatus = repo.GetPCQuestStatusByID(pcGO.getUUID(), questID);

        if(questStatus == null)
        {
            NWScript.sendMessageToPC(oPC, "You have not accepted this quest yet.");
            return;
        }

        if(questStatus.getCompletionDate() != null) return;

        QuestEntity quest = questStatus.getQuest();
        List<QuestStateEntity> states = quest.getQuestStates();

        // Find the next quest state.
        for(QuestStateEntity nextState : states)
        {
            if(nextState.getSequence() == questStatus.getCurrentQuestState().getSequence()+1)
            {
                // Either complete the quest or move to the new state.
                if(nextState.isFinalState())
                {
                    RequestRewardSelectionFromPC(oPC, questID);
                    return;
                }
                else
                {
                    NWScript.addJournalQuestEntry(quest.getJournalTag(), nextState.getJournalStateID(), oPC, false, false, false);
                    questStatus.setCurrentQuestState(nextState);
                    NWScript.sendMessageToPC(oPC, "Objective for quest '" + quest.getName() + "' complete! Check your journal for information on the next objective.");
                    repo.Save(questStatus);

                    CreateExtendedQuestDataEntries(questStatus, questID, nextState.getSequence());
                    return;
                }
            }
        }

        // Shouldn't reach this point unless configuration for the quest is broken.
        NWScript.sendMessageToPC(oPC, "There was an error advancing you to the next objective for quest '" + quest.getName() + "'. Please inform an admin this quest is bugged. (QuestID = " + questID + ")");
    }

    private static void CreateExtendedQuestDataEntries(PCQuestStatusEntity status, int questID, int sequenceID)
    {
        QuestRepository questRepo = new QuestRepository();

        // Create entries for the PC kill targets.
        List<QuestKillTargetListEntity> killTargets = questRepo.GetQuestKillTargetsByQuestSequenceID(questID, sequenceID);
        for(QuestKillTargetListEntity kt : killTargets)
        {
            PCQuestKillTargetProgressEntity pcKT = new PCQuestKillTargetProgressEntity();
            pcKT.setRemainingToKill(kt.getQuantity());
            pcKT.setNpcGroup(kt.getNpcGroup());
            pcKT.setPcQuestStatus(status);
            pcKT.setPlayerID(status.getPlayerID());

            questRepo.Save(pcKT);
        }
    }

    private static void RequestRewardSelectionFromPC(NWObject oPC, int questID)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        QuestRepository questRepo = new QuestRepository();
        QuestEntity quest = questRepo.GetQuestByID(questID);

        if(quest.allowRewardSelection())
        {
            NWScript.setLocalInt(oPC, "QST_REWARD_SELECTION_QUEST_ID", questID);
            DialogManager.startConversation(oPC, oPC, "QuestRewardSelection");
        }
        else
        {
            QuestStateEntity lastState = quest.getQuestStates().get(quest.getQuestStates().size()-1);
            NWScript.addJournalQuestEntry(quest.getJournalTag(), lastState.getJournalStateID(), oPC, false, false, false);
            CompleteQuest(oPC, questID, null);
        }

    }

    public static void CompleteQuest(NWObject oPC, int questID, ItemModel selectedItem)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        String uuid = pcGO.getUUID();
        QuestRepository questRepo = new QuestRepository();
        QuestEntity quest = questRepo.GetQuestByID(questID);
        PCQuestStatusEntity pcState = questRepo.GetPCQuestStatusByID(uuid, questID);

        QuestStateEntity finalState = null;
        for(QuestStateEntity questState : quest.getQuestStates())
        {
            if(questState.isFinalState())
            {
                finalState = questState;
                break;
            }
        }

        if(finalState == null)
        {
            NWScript.sendMessageToPC(oPC, "Could not find final state of quest. Please notify an admin this quest is bugged. (QuestID: " + questID + ")");
            return;
        }

        pcState.setCurrentQuestState(finalState);
        pcState.setCompletionDate(DateTime.now(DateTimeZone.UTC).toDate());

        if(selectedItem == null)
        {
            for(QuestRewardItemEntity reward : quest.getRewardItems())
            {
                NWScript.createItemOnObject(reward.getResref(), oPC, reward.getQuantity(), "");
            }
        }
        else
        {
            NWScript.createItemOnObject(selectedItem.getResref(), oPC, selectedItem.getQuantity(), "");
        }

        if(quest.getRewardGold() > 0)
        {
            NWScript.giveGoldToCreature(oPC, quest.getRewardGold());
        }

        if(quest.getRewardXP() > 0)
        {
            // TODO: Skill-related exp rewards??
        }

        if(quest.getRewardKeyItem() != null)
        {
            KeyItemSystem.GivePlayerKeyItem(oPC, quest.getRewardKeyItem().getKeyItemID());
        }

        if(quest.removeStartKeyItemAfterCompletion())
        {
            KeyItemSystem.RemovePlayerKeyItem(oPC, quest.getStartKeyItem().getKeyItemID());
        }

        if(!quest.getMapNoteTag().equals(""))
        {
            MapPinHelper.DeleteMapPin(oPC, "QST_MAP_NOTE_" + questID);
        }

        if(quest.getRewardFame() > 0)
        {
            FameRepository fameRepo = new FameRepository();
            PCRegionalFameEntity fame = fameRepo.GetPCFameByID(uuid, quest.getFameRegion().getFameRegionID());
            fame.setAmount(fame.getAmount() + quest.getRewardFame());
            fameRepo.Save(fame);
        }

        NWScript.sendMessageToPC(oPC, "Quest '" + quest.getName() + "' complete!");
        questRepo.Save(pcState);
    }

    private static boolean DoesPlayerMeetPrerequisites(NWObject oPC, List<QuestPrerequisiteEntity> prereqs)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return false;
        if(prereqs.isEmpty()) return true;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();
        List<Integer> completedQuestIDs = questRepo.GetAllCompletedQuestIDs(pcGO.getUUID());

        ArrayList<Integer> prereqIDs = new ArrayList<>();
        for(QuestPrerequisiteEntity prereq : prereqs)
        {
            prereqIDs.add(prereq.getRequiredQuest().getQuestID());
        }

        return completedQuestIDs.containsAll(prereqIDs);
    }

    private static boolean DoesPlayerHaveRequiredKeyItems(NWObject oPC, List<QuestRequiredKeyItemListEntity> requiredKeyItems)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return false;
        if(requiredKeyItems.isEmpty()) return true;

        PlayerGO pcGO = new PlayerGO(oPC);
        KeyItemRepository keyItemRepo = new KeyItemRepository();
        List<Integer> keyItemIDs = keyItemRepo.GetListOfPCKeyItemIDs(pcGO.getUUID());

        ArrayList<Integer> requiredKeyItemIDs = new ArrayList<>();
        for(QuestRequiredKeyItemListEntity ki : requiredKeyItems)
        {
            requiredKeyItemIDs.add(ki.getKeyItem().getKeyItemID());
        }

        return keyItemIDs.containsAll(requiredKeyItemIDs);
    }

    static void SpawnQuestItems(NWObject oChest, NWObject oPC)
    {
        int questID = NWScript.getLocalInt(oChest, "QUEST_ID");
        int questStateSequence = NWScript.getLocalInt(oChest, "SEQUENCE_ID");
        String questItemResref = NWScript.getLocalString(oChest, "QUEST_ITEM_RESREF");

        if(questID <= 0 || questStateSequence <= 0 || questItemResref.equals("")) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();

        PCQuestStatusEntity status = questRepo.GetPCQuestStatusByID(pcGO.getUUID(), questID);
        QuestStateEntity questState = status.getCurrentQuestState();

        if(questStateSequence != questState.getSequence()) return;
        if(!NWScript.getIsObjectValid(NWScript.getItemPossessedBy(oPC, questItemResref))) return;

        // PC is on the correct quest, correct state, the chest creates quest items, and the PC does not already have the quest item.
        // Spawn it.

        NWScript.createItemOnObject(questItemResref, oChest, 1, "");

    }

    public static void OnCreatureDeath(NWObject creature)
    {
        int npcGroupID = NWScript.getLocalInt(creature, "NPC_GROUP");
        if (npcGroupID <= 0) return;

        NWObject oKiller = NWScript.getLastKiller();

        if(!NWScript.getIsPC(oKiller) || NWScript.getIsDM(oKiller)) return;

        String areaResref = NWScript.getResRef(NWScript.getArea(creature));

        for(NWObject oPC : NWScript.getFactionMembers(oKiller, true))
        {
            if(!areaResref.equals(NWScript.getResRef(NWScript.getArea(oPC)))) continue;
            if(NWScript.getDistanceBetween(creature, oPC) == 0.0f || NWScript.getDistanceBetween(creature, oPC) > 20.0f) continue;

            PlayerGO pcGO = new PlayerGO(oPC);
            QuestRepository questRepo = new QuestRepository();
            List<PCQuestKillTargetProgressEntity> killTargets = questRepo.GetPlayerKillTargetsByID(pcGO.getUUID(), npcGroupID);

            for(PCQuestKillTargetProgressEntity kt : killTargets)
            {
                kt.setRemainingToKill(kt.getRemainingToKill() - 1);
                String targetGroupName = kt.getNpcGroup().getName();
                String questName = kt.getPcQuestStatus().getQuest().getName();
                String updateMessage = "[" + questName + "] " + targetGroupName + " remaining: " + kt.getRemainingToKill();

                if(kt.getRemainingToKill() <= 0)
                {
                    questRepo.Delete(kt);
                    updateMessage += " " + ColorToken.Green() + " {COMPLETE}";

                    if(kt.getPcQuestStatus().getPcKillTargets().size()-1 <= 0)
                    {
                        AdvanceQuestState(oPC, kt.getPcQuestStatus().getQuest().getQuestID());
                    }
                }
                else
                {
                    questRepo.Save(kt);
                }

                final String finalMessage = updateMessage;
                Scheduler.delay(oPC, 1000, () -> NWScript.sendMessageToPC(oPC, finalMessage));
            }
        }
    }

    private static void HandleTriggerAndPlaceableQuestLogic(NWObject oPC, NWObject oObject)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;
        final String questMessage = NWScript.getLocalString(oObject, "QUEST_MESSAGE");
        int questID = NWScript.getLocalInt(oObject, "QUEST_ID");
        int questSequence = NWScript.getLocalInt(oObject, "QUEST_SEQUENCE");

        if(questID <= 0)
        {
            NWScript.sendMessageToPC(oPC, "QUEST_ID variable not set on object. Please inform admin this quest is bugged.");
            return;
        }

        if(questSequence <= 0)
        {
            NWScript.sendMessageToPC(oPC, "QUEST_SEQUENCE variable not set on object. Please inform admin this quest is bugged. (QuestID: " + questID + ")");
            return;
        }

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();

        PCQuestStatusEntity pcQuestStatus = questRepo.GetPCQuestStatusByID(pcGO.getUUID(), questID);
        if(pcQuestStatus == null) return;

        QuestStateEntity questState = pcQuestStatus.getCurrentQuestState();

        if(questState.getSequence() == questSequence ||
          (questState.getQuestType().getQuestTypeID() != QuestType.UseObject &&
                  questState.getQuestType().getQuestTypeID() != QuestType.ExploreArea))
        {
            return;
        }


        if(!questMessage.equals(""))
        {
            Scheduler.delay(oPC, 1000, () -> NWScript.sendMessageToPC(oPC, questMessage));
        }

        AdvanceQuestState(oPC, questID);
    }

    public static void OnQuestPlaceableUsed(NWObject oObject)
    {
        NWObject oPC = NWScript.getLastUsedBy();
        HandleTriggerAndPlaceableQuestLogic(oPC, oObject);
    }

    public static void OnQuestTriggerEntered(NWObject oObject)
    {
        NWObject oPC = NWScript.getEnteringObject();
        HandleTriggerAndPlaceableQuestLogic(oPC, oObject);
    }

    public static ItemModel GetTempItemInformation(String resref, int quantity)
    {
        NWObject tempStorage = NWScript.getObjectByTag(TempStoragePlaceableTag, 0);
        NWObject tempItem = NWScript.createItemOnObject(resref, tempStorage, 1, "");
        ItemModel model = new ItemModel();
        model.setName(NWScript.getName(tempItem, false));
        model.setQuantity(quantity);
        model.setResref(resref);
        model.setTag(NWScript.getTag(tempItem));
        model.setDescription(NWScript.getDescription(tempItem, false, true));

        return model;
    }

    public static void OnItemCollectorOpened(NWObject container)
    {
        NWScript.setUseableFlag(container, false);

        NWObject oPC = NWScript.getLastOpenedBy();
        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository repo = new QuestRepository();
        int questID = NWScript.getLocalInt(container, "QUEST_ID");
        QuestStateEntity state = repo.GetPCQuestStatusByID(pcGO.getUUID(), questID).getCurrentQuestState();

        NWScript.floatingTextStringOnCreature("Please place the items you would like to turn in for this quest into the container.", oPC, false);
        StringBuilder text = new StringBuilder("Required Items: \n\n");

        for(QuestRequiredItemListEntity item : state.getRequiredItems())
        {
            ItemModel tempItemModel = GetTempItemInformation(item.getResref(), item.getQuantity());
            text.append(tempItemModel.getName()).append(" x").append(item.getQuantity()).append("\n");
        }

        NWScript.sendMessageToPC(oPC, text.toString());
    }

    public static void OnItemCollectorClosed(NWObject container)
    {
        NWObject oPC = NWScript.getLastClosedBy();
        for(NWObject item : NWScript.getItemsInInventory(container))
        {
            if(!NWScript.getResRef(item).equals(SubmitQuestItemResref))
            {
                NWScript.copyItem(item, oPC, true);
            }
            NWScript.destroyObject(item, 0.0f);
        }

        NWScript.destroyObject(container, 0.0f);
    }

    private static void HandleAddItemToItemCollector(NWObject oPC, NWObject container, NWObject oItem)
    {
        String resref = NWScript.getResRef(oItem);
        if(resref.equals(SubmitQuestItemResref)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository repo = new QuestRepository();
        int questID = NWScript.getLocalInt(container, "QUEST_ID");
        QuestStateEntity state = repo.GetPCQuestStatusByID(pcGO.getUUID(), questID).getCurrentQuestState();

        for(QuestRequiredItemListEntity reqItem: state.getRequiredItems())
        {
            if(reqItem.getResref().equals(resref))
            {
                return;
            }
        }

        NWScript.copyItem(oItem, oPC, true);
        NWScript.destroyObject(oItem, 0.0f);
        NWScript.sendMessageToPC(oPC, ColorToken.Red() + "That item is not required for this quest." + ColorToken.End());
    }

    private static void HandleRemoveItemFromItemCollector(NWObject oPC, NWObject container, NWObject oItem)
    {
        String resref = NWScript.getResRef(oItem);
        if(!resref.equals(SubmitQuestItemResref)) return;

        NWScript.destroyObject(oItem, 0.0f);
        NWScript.createItemOnObject(SubmitQuestItemResref, container, 1, "");

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository repo = new QuestRepository();
        int questID = NWScript.getLocalInt(container, "QUEST_ID");
        QuestStateEntity state = repo.GetPCQuestStatusByID(pcGO.getUUID(), questID).getCurrentQuestState();
        NWObject[] submittedItems = NWScript.getItemsInInventory(container);

        HashMap<String, Integer> requiredItems = new HashMap<>();

        // Consolidate the number of items required. This is necessary in case there are two entries for the same
        // item in the database. We simply sum those up to get one result in the hash map.
        for(QuestRequiredItemListEntity ri : state.getRequiredItems())
        {
            if(requiredItems.containsKey(ri.getResref()))
            {
                int count = requiredItems.get(ri.getResref()) + ri.getQuantity();
                requiredItems.replace(ri.getResref(), count);
            }
            else
            {
                requiredItems.put(ri.getResref(), ri.getQuantity());
            }
        }

        for(NWObject item: submittedItems)
        {
            resref = NWScript.getResRef(item);
            if(resref.equals(SubmitQuestItemResref))
                continue;

            int stackSize = NWScript.getItemStackSize(item);

            if(requiredItems.containsKey(resref))
            {
                int amountRequired = requiredItems.get(resref);

                // Same amount - destroy and remove.
                if(stackSize == amountRequired)
                {
                    NWScript.destroyObject(item, 0.0f);
                    requiredItems.remove(resref);
                }
                // Stack > amount - change stack size and remove.
                else if(stackSize > amountRequired)
                {
                    stackSize = stackSize - amountRequired;
                    NWScript.setItemStackSize(item, stackSize);
                    requiredItems.remove(resref);
                    NWScript.copyItem(item, oPC, true);
                }
                // Stack < amount - destroy and update required remaining.
                else if(stackSize < amountRequired)
                {
                    NWScript.destroyObject(item, 0.0f);
                    amountRequired = amountRequired - stackSize;
                    requiredItems.replace(resref, amountRequired);
                }
            }
            // No more items needed.
            else
            {
                NWScript.copyItem(item, oPC, true);
                NWScript.destroyObject(item, 0.0f);
            }
        }

        // Still missing items. Return everything to player and give error message.
        if(!requiredItems.isEmpty())
        {
            for(NWObject item : NWScript.getItemsInInventory(container))
            {
                if(!NWScript.getResRef(item).equals(SubmitQuestItemResref))
                    NWScript.copyItem(item, oPC, true);
                NWScript.destroyObject(item, 0.0f);
            }

            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You are missing some required items..." + ColorToken.End());
        }
        // Succeeded. Take everything and advance the quest state.
        else
        {
            for(NWObject item: NWScript.getItemsInInventory(container))
            {
                NWScript.destroyObject(item, 0.0f);
            }

            AdvanceQuestState(oPC, questID);
        }


        NWScript.destroyObject(container, 0.0f);

    }

    public static void OnItemCollectorDisturbed(NWObject container)
    {
        NWObject oPC = NWScript.getLastDisturbed();
        NWObject oItem = NWScript.getInventoryDisturbItem();
        int disturbType = NWScript.getInventoryDisturbType();

        if(disturbType == InventoryDisturb.TYPE_ADDED)
        {
            HandleAddItemToItemCollector(oPC, container, oItem);
        }
        else if(disturbType == InventoryDisturb.TYPE_REMOVED)
        {
            HandleRemoveItemFromItemCollector(oPC, container, oItem);
        }


    }

    public static void RequestItemsFromPC(NWObject oPC, NWObject questOwner, int questID, int sequenceID)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();
        PCQuestStatusEntity pcStatus = questRepo.GetPCQuestStatusByID(pcGO.getUUID(), questID);


        if(pcStatus == null)
        {
            NWScript.sendMessageToPC(oPC, "You have not accepted this quest yet.");
            return;
        }

        QuestStateEntity questState = pcStatus.getCurrentQuestState();

        if(questState.getSequence() != sequenceID)
        {
            NWScript.sendMessageToPC(oPC, "SequenceID mismatch. Please inform an admin there is a bug with this quest. (QuestID = " + questID + ")");
            return;
        }

        for(QuestRequiredKeyItemListEntity ki : questState.getRequiredKeyItems())
        {
            if(!KeyItemSystem.PlayerHasKeyItem(oPC, ki.getKeyItem().getKeyItemID()))
            {
                NWScript.sendMessageToPC(oPC, "You are missing a required key item.");
                return;
            }
        }

        NWLocation location = NWScript.getLocation(oPC);
        final NWObject collector = NWScript.createObject(ObjectType.PLACEABLE, "qst_item_collect", location, false, "");

        Scheduler.assign(collector, () -> NWScript.setFacingPoint(NWScript.getPosition(oPC)));
        NWScript.setLocalInt(collector, "QUEST_ID", questID);
        NWScript.createItemOnObject(SubmitQuestItemResref, collector, 1, "");

        Scheduler.assign(oPC, () -> NWScript.actionInteractObject(collector));
    }

    public static boolean HasPlayerCompletedQuest(NWObject oPC, int questID) {
        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository repo = new QuestRepository();
        PCQuestStatusEntity status = repo.GetPCQuestStatusByID(pcGO.getUUID(), questID);

        return status != null && status.getCompletionDate() != null;
    }

    public static int GetPlayerQuestJournalID(NWObject oPC, int questID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository repo = new QuestRepository();
        PCQuestStatusEntity status = repo.GetPCQuestStatusByID(pcGO.getUUID(), questID);

        if(status == null) return -1;
        return status.getCurrentQuestState().getJournalStateID();
    }
}
