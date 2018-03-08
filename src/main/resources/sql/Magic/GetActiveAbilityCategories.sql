
SELECT ac.AbilityCategoryID ,
       ac.Name ,
       ac.IsActive
FROM dbo.AbilityCategories ac
WHERE ac.IsActive = 1
ORDER BY ac.Name ASC