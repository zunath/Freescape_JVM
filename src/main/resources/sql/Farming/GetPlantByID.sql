SELECT p.PlantID ,
       p.Name ,
       p.BaseTicks ,
       p.Resref,
       p.WaterTicks,
       p.Level
FROM dbo.Plants p
WHERE p.PlantID = :plantID