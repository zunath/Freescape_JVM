

SELECT TOP(1) bi.BuildingInteriorID ,
       bi.BuildingCategoryID ,
       bi.AreaResref ,
       bi.Name ,
       bi.IsDefaultForCategory
FROM dbo.BuildingInteriors bi
WHERE bi.IsDefaultForCategory = 1
	AND bi.BuildingCategoryID = :buildingCategoryID