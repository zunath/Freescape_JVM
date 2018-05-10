
SELECT pctfs.PCTerritoryFlagStructureID ,
       pctfs.PCTerritoryFlagID ,
       pctfs.StructureBlueprintID ,
       pctfs.IsUseable ,
       pctfs.LocationAreaTag ,
       pctfs.LocationX ,
       pctfs.LocationY ,
       pctfs.LocationZ ,
       pctfs.LocationOrientation ,
       pctfs.CreateDate,
       pctfs.CustomName,
       pctfs.BuildingInteriorID,
       pctfs.IsActive
FROM dbo.PCTerritoryFlagsStructures pctfs
WHERE pctfs.PCTerritoryFlagStructureID = :pcTerritoryFlagStructureID