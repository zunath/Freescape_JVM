
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
WHERE sb.StructureBlueprintID = :structureBlueprintID