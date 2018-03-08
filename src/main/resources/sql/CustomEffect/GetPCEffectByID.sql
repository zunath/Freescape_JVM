

SELECT pcce.PCCustomEffectID ,
       pcce.PlayerID ,
       pcce.CustomEffectID ,
       pcce.Ticks
FROM dbo.PCCustomEffects pcce
WHERE pcce.PlayerID = :playerID
	AND pcce.CustomEffectID = :customEffectID