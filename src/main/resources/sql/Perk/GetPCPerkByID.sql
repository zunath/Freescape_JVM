
SELECT PCPerkID ,
       PlayerID ,
       AcquiredDate ,
       PerkID ,
       PerkLevel
FROM dbo.PCPerks
WHERE PlayerID = :playerID
	AND PerkID = :perkID
