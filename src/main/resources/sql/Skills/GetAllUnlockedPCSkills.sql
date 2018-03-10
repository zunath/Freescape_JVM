
SELECT PCSkillID ,
       PlayerID ,
       SkillID ,
       XP ,
       Rank,
	   IsLocked
FROM dbo.PCSkills
WHERE IsLocked = 0
	AND PlayerID = :playerID
	AND SkillID <> :skillID
	AND (XP > 0 OR Rank > 0)
ORDER BY SkillID
