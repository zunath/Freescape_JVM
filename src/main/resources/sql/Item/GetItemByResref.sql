SELECT Resref ,
       AC ,
       ItemTypeID
FROM dbo.Items
WHERE Resref = :resref