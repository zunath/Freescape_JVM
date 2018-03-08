

SELECT c.CraftID ,
       c.Name ,
       c.IsActive ,
       c.Description
FROM dbo.Crafts c
WHERE c.IsActive = 1