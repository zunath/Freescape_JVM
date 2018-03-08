
BEGIN TRY
    BEGIN TRANSACTION

		DELETE FROM dbo.PlayerProgressionSkills
		WHERE PlayerID = :playerID

    COMMIT TRANSACTION
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN

    RAISERROR('Failed to delete from PlayerProgressionSkills table. (PlayerProgressionSkills/DeleteAllByPlayerID.sql)', -1, -1)
END CATCH