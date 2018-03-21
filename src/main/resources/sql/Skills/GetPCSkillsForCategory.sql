
SELECT pcs.PCSkillID ,
       pcs.PlayerID ,
       pcs.SkillID ,
       pcs.XP ,
       pcs.Rank ,
       pcs.IsLocked 
FROM dbo.PCSkills pcs
JOIN dbo.Skills s ON s.SkillID = pcs.SkillID
WHERE s.IsActive = 1
	AND s.SkillCategoryID = :skillCategoryID
	AND pcs.PlayerID = :playerID