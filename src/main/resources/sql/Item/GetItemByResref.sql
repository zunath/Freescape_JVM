SELECT Resref ,
       AC ,
       ItemTypeID,
       RecommendedLevel
FROM dbo.Items
WHERE Resref = :resref