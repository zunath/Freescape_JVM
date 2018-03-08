
SELECT pctfp.PCTerritoryFlagPermissionID ,
       pctfp.PCTerritoryFlagID ,
       pctfp.PlayerID ,
       pctfp.TerritoryFlagPermissionID
FROM dbo.PCTerritoryFlagsPermissions pctfp
WHERE pctfp.PCTerritoryFlagID = :flagID
	AND pctfp.PlayerID = :playerID