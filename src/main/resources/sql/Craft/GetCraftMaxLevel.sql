
SELECT MAX(cl.Level)
FROM dbo.CraftLevels cl
WHERE cl.CraftID = :craftID