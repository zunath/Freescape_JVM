
SELECT sb.StructureBlueprintID ,
       sb.StructureCategoryID ,
       sb.Name ,
       sb.Description ,
       sb.Resref ,
       sb.IsActive ,
       sb.IsTerritoryFlag ,
       sb.IsUseable ,
       sb.ItemStorageCount ,
       sb.VanityCount ,
       sb.SpecialCount,
       sb.MaxBuildDistance ,
       sb.Level ,
       sb.PerkID ,
       sb.RequiredPerkLevel ,
       sb.GivesSkillXP,
       sb.IsVanity,
       sb.IsSpecial,
       sb.CraftTierLevel
FROM dbo.StructureBlueprints sb
OUTER APPLY dbo.fn_GetPlayerEffectivePerkLevel(:playerID, sb.PerkID, :rank + 2) pcp
WHERE sb.IsActive = 1
	AND sb.StructureCategoryID = :structureCategoryID
	AND (sb.Level <= :rank + 2)
	AND ISNULL(pcp.Level, 0) >= sb.RequiredPerkLevel
	AND sb.IsVanity = :isVanity
	AND sb.IsSpecial = :isSpecial
