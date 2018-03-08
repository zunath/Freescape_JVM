
SELECT pea.PlayerID ,
       pea.Slot1 ,
       pea.Slot2 ,
       pea.Slot3 ,
       pea.Slot4 ,
       pea.Slot5 ,
       pea.Slot6 ,
       pea.Slot7 ,
       pea.Slot8 ,
       pea.Slot9 ,
       pea.Slot10
FROM dbo.PCEquippedAbilities pea
WHERE pea.PlayerID = :playerID