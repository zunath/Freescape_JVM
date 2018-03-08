
SELECT pcqs.PCQuestStatusID ,
       pcqs.QuestID ,
       pcqs.CurrentQuestStateID ,
       pcqs.CompletionDate ,
       pcqs.SelectedItemRewardID ,
       pcqs.PlayerID
FROM dbo.PCQuestStatus pcqs
WHERE pcqs.PlayerID = :playerID
	AND pcqs.QuestID = :questID