

SELECT pcp.PCPerkID ,
       pcp.PlayerID ,
       pcp.AcquiredDate ,
       pcp.PerkID
FROM dbo.PCPerks pcp
JOIN dbo.Perks p ON p.PerkID = pcp.PerkID
WHERE pcp.PlayerID = :playerID
	AND p.PerkCategoryID = :perkCategoryID
ORDER BY a.Name ASC