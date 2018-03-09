
SELECT CAST(MAX(Rank) AS INTEGER) AS MaxRank
FROM dbo.SkillXPRequirement
WHERE SkillID = :skillID