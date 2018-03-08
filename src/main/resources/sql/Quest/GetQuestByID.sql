
SELECT q.QuestID ,
       q.Name ,
       q.JournalTag ,
       q.FameRegionID ,
       q.RequiredFameAmount ,
       q.AllowRewardSelection ,
       q.RewardGold ,
       q.RewardXP ,
       q.RewardKeyItemID ,
       q.RewardFame ,
       q.IsRepeatable ,
       q.MapNoteTag ,
       q.StartKeyItemID ,
       q.RemoveStartKeyItemAfterCompletion
FROM dbo.Quests q
WHERE q.QuestID = :questID