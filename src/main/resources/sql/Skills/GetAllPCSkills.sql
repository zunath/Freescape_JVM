
SELECT ps.PCSkillID ,
       ps.PlayerID ,
       ps.SkillID ,
       ps.XP ,
       ps.Rank,
       ps.IsLocked
FROM dbo.PCSkills ps
JOIN dbo.Skills s ON s.SkillID = ps.SkillID
WHERE ps.PlayerID = :playerID
	AND s.IsActive = 1