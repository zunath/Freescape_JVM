SELECT cbc.CraftBlueprintCategoryID ,
       cbc.Name ,
       cbc.IsActive
FROM dbo.CraftBlueprintCategories cbc
WHERE cbc.CraftBlueprintCategoryID IN (
	SELECT DISTINCT cb2.CraftCategoryID
	FROM dbo.CraftBlueprints AS cb2
	JOIN dbo.PCSkills pcs ON pcs.PlayerID = :playerID
	LEFT JOIN dbo.PCPerks pcp ON (cb2.PerkID IS NULL OR pcp.PerkID = cb2.PerkID)
		AND (pcp.PerkLevel >= cb2.RequiredPerkLevel)
		AND (pcs.PlayerID = pcp.PlayerID)
	WHERE cb2.IsActive = 1
		AND (cb2.Level <= pcs.Rank+2)
		AND (pcp.PCPerkID IS NOT NULL OR cb2.PerkID IS NULL)
)
	AND cbc.IsActive = 1
ORDER BY cbc.Name ASC