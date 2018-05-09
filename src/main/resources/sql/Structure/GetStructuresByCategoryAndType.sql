DECLARE @rank AS INT = (SELECT Rank FROM dbo.PCSkills WHERE PlayerID = :playerID AND SkillID = 15) + 2

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
       sb.CraftTierLevel,
       sb.ResourceCount,
       sb.BuildingCount,
       sb.IsResource,
       sb.IsBuilding,
       sb.ResourceResref,
       sb.BuildingCategoryID
FROM dbo.StructureBlueprints sb
OUTER APPLY dbo.fn_GetPlayerEffectivePerkLevel(:playerID, sb.PerkID, @rank) pcp
WHERE sb.IsActive = 1
	AND sb.StructureCategoryID = :structureCategoryID
	AND (sb.Level <= @rank)
	AND ISNULL(pcp.Level, 0) >= sb.RequiredPerkLevel
	AND sb.IsVanity = :isVanity
	AND sb.IsSpecial = :isSpecial
    AND sb.IsResource = :isResource
    AND sb.IsBuilding = :isBuilding