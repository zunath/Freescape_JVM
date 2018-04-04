SELECT p.PlantID ,
       p.Name ,
       p.BaseTicks ,
       p.Resref
FROM dbo.Plants p
WHERE p.PlantID = :plantID