
SELECT sb.StructureBlueprintID ,
       sb.StructureCategoryID ,
       sb.Name ,
       sb.Description ,
       sb.Resref ,
       sb.IsActive ,
       sb.IsTerritoryFlag ,
       sb.IsUseable ,
       sb.ItemStorageCount ,
       sb.MaxStructuresCount ,
       sb.MaxBuildDistance ,
       sb.ResearchSlots ,
       sb.RPPerSecond,
       sb.Level,
	   sb.PerkID,
	   sb.RequiredPerkLevel,
	   sb.GivesSkillXP
FROM dbo.StructureBlueprints AS sb
WHERE sb.StructureBlueprintID IN (
	SELECT DISTINCT sb2.StructureBlueprintID
	FROM dbo.StructureBlueprints AS sb2
	LEFT JOIN dbo.PCPerks pcp ON (sb2.PerkID IS NULL OR pcp.PerkID = sb2.PerkID) AND (pcp.PerkLevel >= sb2.RequiredPerkLevel)
	WHERE sb2.IsActive = 1
		AND sb2.StructureCategoryID = :structureCategoryID
		AND (sb2.Level <= :rank + 2 )
)
ORDER BY sb.Name ASC
