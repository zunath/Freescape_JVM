

SELECT CreatureID ,
       DifficultyRating ,
       XPModifier
FROM dbo.Creatures
WHERE CreatureID = :creatureID