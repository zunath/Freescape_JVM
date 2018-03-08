
SELECT pcki.PCKeyItemID ,
       pcki.PlayerID ,
       pcki.KeyItemID ,
       pcki.AcquiredDate
FROM dbo.PCKeyItems pcki
WHERE pcki.PlayerID = :playerID
	AND pcki.KeyItemID = :keyItemID