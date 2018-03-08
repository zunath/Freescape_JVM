
SELECT sb.StructureBlueprintID ,
       sb.StructureCategoryID ,
       sb.Name ,
       sb.Description ,
       sb.Resref ,
       sb.IsActive ,
       sb.IsTerritoryFlag ,
       sb.IsUseable ,
       sb.WoodRequired ,
       sb.MetalRequired ,
       sb.NailsRequired ,
       sb.ClothRequired ,
       sb.LeatherRequired ,
       sb.ItemStorageCount ,
       sb.MaxStructuresCount ,
       sb.MaxBuildDistance ,
       sb.IronRequired ,
       sb.ResearchSlots ,
       sb.RPPerSecond
FROM dbo.StructureBlueprints sb
WHERE sb.IsActive = 1
	AND sb.StructureCategoryID = :structureCategoryID
ORDER BY sb.Name ASC