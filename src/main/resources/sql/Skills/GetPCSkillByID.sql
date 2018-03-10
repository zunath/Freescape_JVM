SELECT PCSkillID ,
       PlayerID ,
       SkillID ,
       XP ,
       Rank,
       IsLocked
FROM dbo.PCSkills
WHERE SkillID = :skillID
	AND PlayerID = :playerID