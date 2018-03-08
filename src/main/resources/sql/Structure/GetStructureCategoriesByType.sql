
SELECT sc.StructureCategoryID ,
       sc.Name ,
       sc.Description ,
       sc.IsActive ,
       sc.IsTerritoryFlagCategory
FROM dbo.StructureCategories sc
WHERE sc.IsTerritoryFlagCategory = :isTerritoryFlagCategory
	AND sc.IsActive = 1
ORDER BY sc.Name ASC