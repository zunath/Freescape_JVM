
SELECT pcqs.QuestID
FROM dbo.PCQuestStatus pcqs
WHERE pcqs.PlayerID = :playerID
	AND pcqs.CompletionDate IS NOT NULL
