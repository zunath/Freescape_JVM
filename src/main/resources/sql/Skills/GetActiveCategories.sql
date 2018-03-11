SELECT SkillCategoryID ,
       Name ,
       IsActive,
       Sequence
FROM dbo.SkillCategories
WHERE IsActive = 1
ORDER BY Sequence