SELECT SUM(Rank) AS TotalSkillPoints
FROM dbo.PCSkills
WHERE PlayerID = :playerID