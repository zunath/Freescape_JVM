

BEGIN TRY
    BEGIN TRANSACTION

		UPDATE dbo.PCTerritoryFlags
		SET PlayerID = :newOwnerPlayerID
		WHERE PCTerritoryFlagID IN (
			SELECT tf2.PCTerritoryFlagID ,
				   tf2.PlayerID ,
				   tf2.StructureBlueprintID ,
				   tf2.LocationAreaTag ,
				   tf2.LocationX ,
				   tf2.LocationY ,
				   tf2.LocationZ ,
				   tf2.LocationOrientation ,
				   tf2.BuildPrivacySettingID ,
				   tf2.ShowOwnerName ,
				   tf2.BuildingPCStructureID
			FROM dbo.PCTerritoryFlags pctf
			JOIN dbo.PCTerritoryFlagsStructures pctfs ON pctfs.PCTerritoryFlagID = pctf.PCTerritoryFlagID
			JOIN dbo.PCTerritoryFlags tf2 ON tf2.BuildingPCStructureID = pctfs.PCTerritoryFlagStructureID
			WHERE pctfs.PCTerritoryFlagID = :flagID
		)

    COMMIT TRANSACTION
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN

    -- you can Raise ERROR with RAISEERROR() Statement including the details of the exception
    RAISERROR('Failed to update PCTerritoryFlags table. (Structure/UpdateBuildingTerritoryFlagsOwner.sql)', -1, -1)
END CATCH