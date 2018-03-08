
SELECT cl.CraftLevelID ,
       cl.CraftID ,
       cl.Level ,
       cl.Experience
FROM dbo.CraftLevels cl
WHERE cl.CraftID = :craftID
	AND cl.Level = :level