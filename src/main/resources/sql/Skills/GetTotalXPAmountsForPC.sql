

SELECT pcs.SkillID, CAST(SUM(xp.XP) + pcs.XP AS INTEGER) AS TotalSkillXP
FROM dbo.PCSkills pcs
JOIN dbo.SkillXPRequirement xp ON xp.SkillID = pcs.SkillID AND ((xp.Rank < pcs.Rank) OR (xp.Rank = 0 AND pcs.XP > 0))
WHERE pcs.IsLocked = 0
	AND pcs.PlayerID = :playerID
	AND pcs.SkillID <> :skillID
GROUP BY pcs.SkillID, pcs.XP
ORDER BY pcs.SkillID



