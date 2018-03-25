SELECT TOP(1) cb2.CraftBlueprintID ,
       cb2.CraftCategoryID ,
       cb2.Level ,
       cb2.ItemName ,
       cb2.ItemResref ,
       cb2.Quantity ,
       cb2.SkillID ,
       cb2.CraftDeviceID ,
       cb2.PerkID ,
       cb2.RequiredPerkLevel ,
       cb2.IsActive,
       cb2.CraftTierLevel
FROM dbo.CraftBlueprints AS cb2
JOIN dbo.PCSkills pcs ON pcs.PlayerID = :playerID
LEFT JOIN dbo.PCPerks pcp ON (cb2.PerkID IS NULL OR pcp.PerkID = cb2.PerkID)
	AND (pcp.PerkLevel >= cb2.RequiredPerkLevel)
	AND (pcs.PlayerID = pcp.PlayerID)
WHERE cb2.IsActive = 1
	AND (cb2.Level <= pcs.Rank+2)
	AND (pcp.PCPerkID IS NOT NULL OR cb2.PerkID IS NULL)
	AND cb2.CraftDeviceID = :deviceID
	AND cb2.CraftBlueprintID = :blueprintID