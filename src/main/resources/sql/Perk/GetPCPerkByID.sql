
SELECT pcla.PCLearnedAbilityID ,
       pcla.PlayerID ,
       pcla.AcquiredDate ,
       pcla.AbilityID
FROM dbo.PCLearnedAbilities pcla
WHERE pcla.AbilityID = :abilityID
	AND pcla.PlayerID = :playerID