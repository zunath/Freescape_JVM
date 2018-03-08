
BEGIN TRY
    BEGIN TRANSACTION
		INSERT INTO dbo.PCSkills ( PlayerID ,
		                           SkillID ,
		                           XP ,
		                           Rank )
        SELECT :playerID,
            s.SkillID,
            0,
            0
        FROM dbo.Skills s
        WHERE s.SkillID NOT IN (
            SELECT pcs.SkillID
            FROM dbo.PCSkills pcs
            WHERE pcs.PlayerID = :playerID
        )

    COMMIT TRANSACTION
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRAN

    RAISERROR('Failed to insert all PC Skills from OnModuleEnter. (Skills/InsertAllPCSkillsByID.sql)', -1, -1)
END CATCH