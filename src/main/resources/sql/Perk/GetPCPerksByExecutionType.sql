
SELECT pcp.PCPerkID ,
       pcp.PlayerID ,
       pcp.AcquiredDate ,
       pcp.PerkID ,
       pcp.PerkLevel
FROM dbo.PCPerks pcp
CROSS APPLY dbo.fn_GetPlayerEffectivePerkLevel(pcp.PlayerID, pcp.PerkID, NULL) ap
JOIN dbo.Perks p ON p.PerkID = pcp.PerkID
WHERE pcp.PlayerID = :playerID
	AND p.ExecutionTypeID = :executionTypeID