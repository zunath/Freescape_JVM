

SELECT pcla.PCLearnedAbilityID ,
       pcla.PlayerID ,
       pcla.AcquiredDate ,
       pcla.AbilityID
FROM dbo.PCLearnedAbilities pcla
JOIN dbo.Abilities a ON a.AbilityID = pcla.AbilityID
WHERE pcla.PlayerID = :playerID
	AND a.AbilityCategoryID = :abilityCategoryID
ORDER BY a.Name ASC