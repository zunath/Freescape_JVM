
SELECT pctf.PCTerritoryFlagID ,
       pctf.PlayerID ,
       pctf.StructureBlueprintID ,
       pctf.LocationAreaTag ,
       pctf.LocationX ,
       pctf.LocationY ,
       pctf.LocationZ ,
       pctf.LocationOrientation ,
       pctf.BuildPrivacySettingID,
       pctf.ShowOwnerName
FROM dbo.PCTerritoryFlags pctf
WHERE pctf.PCTerritoryFlagID = :pcTerritoryFlagID