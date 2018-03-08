
SELECT cs.ConstructionSiteID ,
       cs.PCTerritoryFlagID ,
       cs.PlayerID ,
       cs.StructureBlueprintID ,
       cs.WoodRequired ,
       cs.MetalRequired ,
       cs.NailsRequired ,
       cs.ClothRequired ,
       cs.LeatherRequired ,
       cs.LocationAreaTag ,
       cs.LocationX ,
       cs.LocationY ,
       cs.LocationZ ,
       cs.LocationOrientation ,
       cs.IronRequired
FROM dbo.ConstructionSites cs
