SELECT Resref ,
       AC ,
       ItemTypeID,
       RecommendedLevel,
       LoggingBonus,
       MiningBonus
FROM dbo.Items
WHERE Resref = :resref