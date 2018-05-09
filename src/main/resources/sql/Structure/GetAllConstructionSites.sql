
SELECT cs.ConstructionSiteID ,
       cs.PCTerritoryFlagID ,
       cs.PlayerID ,
       cs.StructureBlueprintID ,
       cs.LocationAreaTag ,
       cs.LocationX ,
       cs.LocationY ,
       cs.LocationZ ,
       cs.LocationOrientation,
       cs.BuildingInteriorID
FROM dbo.ConstructionSites cs
LEFT JOIN dbo.PCTerritoryFlags pctf ON pctf.PCTerritoryFlagID = cs.PCTerritoryFlagID
WHERE cs.PCTerritoryFlagID IS NULL
	OR pctf.BuildingPCStructureID IS NULL