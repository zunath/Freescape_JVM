
SELECT sc.StructureCategoryID ,
       sc.Name ,
       sc.Description ,
       sc.IsActive ,
       sc.IsTerritoryFlagCategory
FROM dbo.StructureCategories sc
WHERE sc.StructureCategoryID IN (
	SELECT DISTINCT sb2.StructureCategoryID
	FROM dbo.StructureBlueprints AS sb2
	JOIN dbo.PCSkills pcs ON pcs.PlayerID = :playerID
	LEFT JOIN dbo.PCPerks pcp ON (sb2.PerkID IS NULL OR pcp.PerkID = sb2.PerkID)
		AND (pcp.PerkLevel >= sb2.RequiredPerkLevel)
		AND (pcs.PlayerID = pcp.PlayerID)
	WHERE sb2.IsActive = 1
		AND sb2.IsTerritoryFlag = :isTerritoryFlagCategory
		AND (sb2.Level <= pcs.Rank+1)
)
ORDER BY sc.Name ASC
