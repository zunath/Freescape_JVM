SELECT p.PlantID ,
       p.Name ,
       p.BaseTicks ,
       p.Resref,
       p.WaterTicks,
       p.Level,
       p.SeedResref
FROM dbo.Plants p
WHERE p.PlantID = :plantID