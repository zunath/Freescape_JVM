
SELECT qktl.QuestKillTargetListID ,
       qktl.QuestID ,
       qktl.NPCGroupID ,
       qktl.Quantity,
       qktl.QuestStateID
FROM dbo.QuestKillTargetList qktl
WHERE qktl.QuestID = :questID
  AND qktl.QuestStateID = (SELECT QuestStateID
                           FROM QuestStates qs
                           WHERE qs.QuestID = :questID
                              AND qs.Sequence = :sequenceID)
