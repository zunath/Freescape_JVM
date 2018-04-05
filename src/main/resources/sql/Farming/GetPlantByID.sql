SELECT p.PlantID ,
       p.Name ,
       p.BaseTicks ,
       p.Resref,
       p.WaterTicks
FROM dbo.Plants p
WHERE p.PlantID = :plantID