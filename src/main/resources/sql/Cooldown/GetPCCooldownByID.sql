
SELECT pcac.PCCooldownID ,
       pcac.PlayerID ,
       pcac.CooldownCategoryID ,
       pcac.DateUnlocked
FROM dbo.PCCooldowns pcac
WHERE pcac.PlayerID = :playerID
	AND pcac.CooldownCategoryID = :cooldownCategoryID