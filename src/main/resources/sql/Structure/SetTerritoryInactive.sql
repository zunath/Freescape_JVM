

BEGIN TRY
    BEGIN TRANSACTION
		DECLARE @temp AS TABLE(
			BuildingPCTerritoryFlagID INT
		)

		-- Add the territory we're deleting
		INSERT INTO @temp ( BuildingPCTerritoryFlagID )
		VALUES ( :flagID )

		-- Add any territories linked via buildings to the delete list
		INSERT INTO @temp ( BuildingPCTerritoryFlagID )
		SELECT pctf.PCTerritoryFlagID
		FROM dbo.PCTerritoryFlagsStructures pctfs
		JOIN dbo.PCTerritoryFlags pctf ON pctf.BuildingPCStructureID = pctfs.PCTerritoryFlagStructureID
		WHERE pctfs.PCTerritoryFlagID = :flagID

		-- Deactivate all flags
		UPDATE dbo.PCTerritoryFlags
		SET IsActive = 0
		WHERE PCTerritoryFlagID IN (SELECT BuildingPCTerritoryFlagID FROM @temp )

		-- Deactivate all structures
		UPDATE dbo.PCTerritoryFlagsStructures
		SET IsActive = 0
		WHERE PCTerritoryFlagID IN (SELECT BuildingPCTerritoryFlagID FROM @temp )

		-- Deactive all construction sites
		UPDATE dbo.ConstructionSites
		SET IsActive = 0
		WHERE PCTerritoryFlagID IN (SELECT BuildingPCTerritoryFlagID FROM @temp )


    COMMIT TRANSACTION
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN

    -- you can Raise ERROR with RAISEERROR() Statement including the details of the exception
    RAISERROR('Failed to mark territory inactive. (Structure/SetTerritoryInactive.sql)', -1, -1)
END CATCH