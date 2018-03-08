
SELECT pcqktp.PCQuestKillTargetProgressID ,
       pcqktp.PlayerID ,
       pcqktp.PCQuestStatusID ,
       pcqktp.NPCGroupID ,
       pcqktp.RemainingToKill ,
       ng.NPCGroupID ,
       ng.Name
FROM dbo.PCQuestKillTargetProgress pcqktp
JOIN dbo.NPCGroups ng ON ng.NPCGroupID = pcqktp.NPCGroupID
WHERE pcqktp.PlayerID = :playerID
	AND ng.NPCGroupID = :npcGroupID
