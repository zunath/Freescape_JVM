
SELECT COUNT(1) AS TotalPerkCount
FROM dbo.PCPerks
WHERE PlayerID = :playerID