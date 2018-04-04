
SELECT gp.GrowingPlantID ,
       gp.PlantID ,
       gp.RemainingTicks ,
       gp.LocationAreaTag ,
       gp.LocationX ,
       gp.LocationY ,
       gp.LocationZ ,
       gp.LocationOrientation ,
       gp.DateCreated ,
       gp.IsActive
FROM dbo.GrowingPlants gp
WHERE gp.IsActive = 1