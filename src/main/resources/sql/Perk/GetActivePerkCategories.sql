
SELECT pc.PerkCategoryID ,
       pc.Name ,
       pc.IsActive
FROM dbo.PerkCategories pc
WHERE pc.IsActive = 1
ORDER BY pc.Name ASC