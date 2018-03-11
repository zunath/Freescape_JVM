

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
       sb.RPPerSecond
FROM dbo.StructureBlueprints sb
WHERE sb.StructureBlueprintID = :structureBlueprintID