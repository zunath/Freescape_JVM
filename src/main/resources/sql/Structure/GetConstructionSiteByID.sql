
SELECT cs.ConstructionSiteID ,
       cs.PCTerritoryFlagID ,
       cs.PlayerID ,
       cs.StructureBlueprintID ,
       cs.LocationAreaTag ,
       cs.LocationX ,
       cs.LocationY ,
       cs.LocationZ ,
       cs.LocationOrientation,
       cs.BuildingInteriorID,
       cs.IsActive
FROM dbo.ConstructionSites cs
WHERE cs.ConstructionSiteID = :constructionSiteID