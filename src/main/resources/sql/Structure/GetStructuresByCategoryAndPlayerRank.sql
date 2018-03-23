
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
       sb.Level ,
       sb.PerkID ,
       sb.RequiredPerkLevel ,
       sb.GivesSkillXP
FROM dbo.StructureBlueprints sb
OUTER APPLY dbo.fn_GetPlayerEffectivePerkLevel(:playerID, sb.PerkID, :rank + 2) pcp
WHERE sb.IsActive = 1
	AND sb.StructureCategoryID = :structureCategoryID
	AND (sb.Level <= :rank + 2)
	AND ISNULL(pcp.Level, 0) >= sb.RequiredPerkLevel
