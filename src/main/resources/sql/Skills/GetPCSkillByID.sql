SELECT PCSkillID ,
       PlayerID ,
       SkillID ,
       XP ,
       Rank
FROM dbo.PCSkills
WHERE SkillID = :skillID
	AND PlayerID = :playerID