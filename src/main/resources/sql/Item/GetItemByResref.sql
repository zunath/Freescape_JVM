SELECT Resref ,
       AC ,
       ItemType
FROM dbo.Items
WHERE Resref = :resref