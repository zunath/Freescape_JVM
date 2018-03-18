SELECT Resref ,
       AC ,
       ItemTypeID,
       RecommendedLevel,
       LoggingBonus,
       MiningBonus,
       CastingSpeed
FROM dbo.Items
WHERE Resref = :resref