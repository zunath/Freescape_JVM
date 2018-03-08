
SELECT pcac.PCAbilityCooldownID ,
       pcac.PlayerID ,
       pcac.AbilityCooldownCategoryID ,
       pcac.DateUnlocked
FROM dbo.PCAbilityCooldowns pcac
WHERE pcac.PlayerID = :playerID
	AND pcac.AbilityCooldownCategoryID = :abilityCooldownCategoryID