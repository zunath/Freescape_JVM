
SELECT 1 AS ID,
	(SELECT TOP(1) pctf.PlayerID AS TerritoryOwner
		FROM dbo.PCTerritoryFlags pctf
		WHERE pctf.PCTerritoryFlagID = :territoryFlagID
		ORDER BY pctf.PCTerritoryFlagID) AS TerritoryOwner,
		(SELECT TOP(1) pctf2.PlayerID AS BuildingOwner
		 FROM dbo.PCTerritoryFlags pctf2
		 WHERE pctf2.BuildingPCStructureID = :buildingStructureID
		 ORDER BY pctf2.PCTerritoryFlagID) AS BuildingOwner
