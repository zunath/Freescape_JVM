
BEGIN TRY
    BEGIN TRANSACTION

		DELETE FROM dbo.StorageItems
		WHERE GlobalID = :globalID

    COMMIT TRANSACTION
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN

    -- you can Raise ERROR with RAISEERROR() Statement including the details of the exception
    RAISERROR('Failed to delete from StorageItems table. (Storage/DeleteStorageItemByGlobalID.sql)', -1, -1)
END CATCH