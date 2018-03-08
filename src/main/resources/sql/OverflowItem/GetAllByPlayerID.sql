

SELECT pcoi.PCOverflowItemID ,
       pcoi.PlayerID ,
       pcoi.ItemName ,
       pcoi.ItemTag ,
       pcoi.ItemResref ,
       pcoi.ItemObject
FROM dbo.PCOverflowItems pcoi
WHERE pcoi.PlayerID = :playerID