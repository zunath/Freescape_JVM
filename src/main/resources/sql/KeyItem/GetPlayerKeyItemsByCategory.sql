
SELECT pcki.PCKeyItemID ,
       pcki.PlayerID ,
       pcki.KeyItemID ,
       pcki.AcquiredDate
FROM dbo.PCKeyItems pcki
JOIN dbo.KeyItems ki ON ki.KeyItemID = pcki.KeyItemID
WHERE pcki.PlayerID = :playerID
	AND ki.KeyItemCategoryID = :categoryID