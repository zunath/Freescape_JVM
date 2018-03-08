
BEGIN TRY
    BEGIN TRANSACTION

		DELETE FROM dbo.PCTerritoryFlagsStructuresResearchQueues
		WHERE PCStructureID = :pcStructureID

    COMMIT TRANSACTION
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN

    RAISERROR('Failed to delete from PCTerritoryFlagsStructuresResearchQueues table. (ResearchRepository/DeleteAllByStructureID.sql)', -1, -1)
END CATCH