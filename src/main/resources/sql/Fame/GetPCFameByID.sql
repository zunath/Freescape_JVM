

SELECT pcf.PCRegionalFameID ,
       pcf.PlayerID ,
       pcf.FameRegionID ,
       pcf.Amount
FROM dbo.PCRegionalFame pcf
JOIN dbo.FameRegions fr ON fr.FameRegionID = pcf.FameRegionID
WHERE pcf.PlayerID = :playerID
	AND fr.FameRegionID = :fameRegionID