

SELECT pcp.PCPerkID,
	   p.Name AS Name,
       pcp.PerkLevel AS Level,
	   pl.Description AS BonusDescription
FROM dbo.PCPerks pcp
JOIN dbo.Perks p ON p.PerkID = pcp.PerkID
JOIN dbo.PerkLevels pl ON pl.PerkID = p.PerkID AND pl.Level = pcp.PerkLevel
WHERE pcp.PlayerID = :playerID
ORDER BY p.Name