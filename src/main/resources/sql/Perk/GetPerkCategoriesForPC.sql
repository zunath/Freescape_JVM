
SELECT pc.PerkCategoryID ,
       pc.Name ,
       pc.IsActive ,
       pc.Sequence
FROM dbo.PerkCategories pc
WHERE pc.PerkCategoryID IN (
	SELECT DISTINCT pc.PerkCategoryID
	FROM dbo.PerkCategories pc
	JOIN dbo.Perks p ON p.PerkCategoryID = pc.PerkCategoryID
	JOIN dbo.PlayerCharacters plc ON plc.PlayerID = :playerID
	LEFT JOIN dbo.PerkLevels pl ON pl.PerkID = p.PerkID
	LEFT JOIN dbo.PerkLevelSkillRequirements psr ON psr.PerkLevelID = pl.PerkLevelID
	LEFT JOIN dbo.PCSkills pcsk ON pcsk.PlayerID = plc.PlayerID AND pcsk.SkillID = psr.SkillID
	WHERE (psr.SkillID IS NULL OR psr.SkillID = pcsk.SkillID)
		AND (psr.RequiredRank IS NULL OR psr.RequiredRank <= pcsk.Rank)
		AND pc.IsActive = 1
)
ORDER BY pc.Sequence
