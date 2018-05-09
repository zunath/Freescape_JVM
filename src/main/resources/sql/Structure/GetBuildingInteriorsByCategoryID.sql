SELECT bi.BuildingInteriorID ,
       bi.BuildingCategoryID ,
       bi.AreaResref ,
       bi.Name ,
       bi.IsDefaultForCategory
FROM dbo.BuildingInteriors bi
WHERE bi.BuildingCategoryID = :buildingCategoryID