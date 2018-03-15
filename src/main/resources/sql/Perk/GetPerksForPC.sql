
SELECT p.PerkID ,
       p.Name ,
       p.FeatID ,
       p.IsActive ,
       p.JavaScriptName ,
       p.BaseManaCost ,
       p.BaseCastingTime ,
       p.Description ,
       p.PerkCategoryID ,
       p.CooldownCategoryID ,
       p.ExecutionTypeID,
       p.IsTargetSelfOnly
FROM dbo.Perks p
WHERE p.PerkID IN (
	SELECT DISTINCT p.PerkID
	FROM dbo.Perks p
	JOIN dbo.PlayerCharacters plc ON plc.PlayerID = :playerID
	LEFT JOIN dbo.PerkLevels pl ON pl.PerkID = p.PerkID
	LEFT JOIN dbo.PerkLevelSkillRequirements plsr ON plsr.PerkLevelID = pl.PerkLevelID
	LEFT JOIN dbo.PCSkills pcsk ON pcsk.PlayerID = plc.PlayerID AND plsr.SkillID = pcsk.SkillID
	WHERE (plsr.SkillID IS NULL OR plsr.SkillID = pcsk.SkillID)
		AND (plsr.RequiredRank IS NULL OR plsr.RequiredRank <= pcsk.Rank)
		AND p.PerkCategoryID = :categoryID
)
