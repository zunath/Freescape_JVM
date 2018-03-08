
BEGIN TRY
    BEGIN TRANSACTION

		DELETE FROM dbo.PCOverflowItems
		WHERE PCOverflowItemID = :pcOverflowItemID

    COMMIT TRANSACTION
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN

    -- you can Raise ERROR with RAISEERROR() Statement including the details of the exception
    RAISERROR('Failed to delete from PCOverflowItems table. (OverflowItem/DeleteByID.sql)', -1, -1)
END CATCH