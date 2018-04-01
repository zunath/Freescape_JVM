SELECT sc.StructureCategoryID ,
       sc.Name ,
       sc.Description ,
       sc.IsActive ,
       sc.IsTerritoryFlagCategory
FROM dbo.StructureCategories sc
WHERE sc.StructureCategoryID IN (
	SELECT DISTINCT sb2.StructureCategoryID
	FROM dbo.StructureBlueprints AS sb2
	JOIN dbo.PCSkills pcs ON pcs.PlayerID = :playerID AND pcs.SkillID = 15
	OUTER APPLY dbo.fn_GetPlayerEffectivePerkLevel(pcs.PlayerID, sb2.PerkID, pcs.Rank+2) pcp
	WHERE sb2.IsActive = 1
		AND sb2.IsTerritoryFlag = :isTerritoryFlagCategory
		AND (sb2.Level <= pcs.Rank+2)
		AND sb2.IsVanity = :isVanity
		AND sb2.IsSpecial = :isSpecial
		AND ((pcp.PerkLevelID IS NOT NULL OR sb2.PerkID IS NULL) OR (pcp.Level >= sb2.RequiredPerkLevel))
)
ORDER BY sc.Name ASC
