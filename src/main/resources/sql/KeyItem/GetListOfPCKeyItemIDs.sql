
SELECT ki.KeyItemID
FROM dbo.PCKeyItems pcki
JOIN dbo.KeyItems ki ON ki.KeyItemID = pcki.KeyItemID
WHERE pcki.PlayerID = :playerID