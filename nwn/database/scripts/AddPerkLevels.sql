
-- Example script used to quickly add perk levels and skill requirements.
BEGIN TRANSACTION

DECLARE @PerkLevelID INT
DECLARE @SkillID INT = 0 -- Skills table
DECLARE @PerkID INT = 0 -- Perks table

INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         1 , -- Level - int
         1 , -- Price - int
         N'' -- Description - nvarchar(512)
    )

SET @PerkLevelID = SCOPE_IDENTITY()

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         10   -- RequiredRank - int
    )

INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         2 , -- Level - int
         1 , -- Price - int
         N'' -- Description - nvarchar(512)
    )
SET @PerkLevelID = SCOPE_IDENTITY()

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         20   -- RequiredRank - int
    )

INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         3 , -- Level - int
         2 , -- Price - int
         N'' -- Description - nvarchar(512)
    )
SET @PerkLevelID = SCOPE_IDENTITY()

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         30   -- RequiredRank - int
    )
INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         4 , -- Level - int
         2 , -- Price - int
         N'' -- Description - nvarchar(512)
    )
SET @PerkLevelID = SCOPE_IDENTITY()

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         40   -- RequiredRank - int
    )

INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         5 , -- Level - int
         3 , -- Price - int
         N'' -- Description - nvarchar(512)
    )
SET @PerkLevelID = SCOPE_IDENTITY()

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         50   -- RequiredRank - int
    )
INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         6 , -- Level - int
         3 , -- Price - int
         N'' -- Description - nvarchar(512)
    )

SET @PerkLevelID = SCOPE_IDENTITY()

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         60   -- RequiredRank - int
    )
-- rollback
-- commit