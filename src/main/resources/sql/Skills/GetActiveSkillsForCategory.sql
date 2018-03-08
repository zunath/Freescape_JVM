SELECT SkillID ,
       SkillCategoryID ,
       Name ,
       MaxRank ,
       IsActive ,
       Description ,
       [Primary] ,
       Secondary ,
       Tertiary 
FROM dbo.Skills
WHERE IsActive = 1 
	AND SkillCategoryID = :skillCategoryID