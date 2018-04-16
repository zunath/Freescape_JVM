
BEGIN TRY
    BEGIN TRANSACTION

		DELETE FROM dbo.PCMapPins
		WHERE PlayerID = :playerID

    COMMIT TRANSACTION
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN

    -- you can Raise ERROR with RAISEERROR() Statement including the details of the exception
    RAISERROR('Failed to delete from PCMapPins table. (MapPin/DeletePlayerMapPins.sql)', -1, -1)
END CATCH