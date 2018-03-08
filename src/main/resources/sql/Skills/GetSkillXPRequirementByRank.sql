
SELECT SkillXPRequirementID ,
       SkillID ,
       Rank ,
       XP
FROM dbo.SkillXPRequirement
WHERE SkillID = :skillID
	AND Rank = :rank