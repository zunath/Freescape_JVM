

BEGIN TRANSACTION

DECLARE @PerkID INT = (SELECT MAX(PerkID) + 1 FROM dbo.Perks)
DECLARE @Name NVARCHAR(64) = 'Holy Shot'
DECLARE @PerkDescription NVARCHAR(256) = N'Shoots a beam at a target, dealing damage.'
DECLARE @PerkJS NVARCHAR(64) = 'Alteration.HolyShot'
DECLARE @SkillID INT = 20
DECLARE @PerkCategoryID INT = 30

INSERT INTO dbo.Perks ( PerkID ,
                        Name ,
                        FeatID ,
                        IsActive ,
                        JavaScriptName ,
                        BaseManaCost ,
                        BaseCastingTime ,
                        Description ,
                        PerkCategoryID ,
                        CooldownCategoryID ,
                        ExecutionTypeID ,
                        ItemResref ,
                        IsTargetSelfOnly )
VALUES ( @PerkID ,    -- PerkID - int
         @Name ,   -- Name - varchar(64)
         NULL ,    -- FeatID - int
         1 , -- IsActive - bit
         @PerkJS ,   -- JavaScriptName - varchar(64)
         8 ,    -- BaseManaCost - int
         4.5 ,  -- BaseCastingTime - float
         @PerkDescription,
         @PerkCategoryID ,    -- PerkCategoryID - int
         12 ,    -- CooldownCategoryID - int
         3 ,    -- ExecutionTypeID - int
         'perk_holyshot'  ,  -- ItemResref - nvarchar(16)
         0   -- IsTargetSelfOnly - bit
    )

DECLARE @PerkLevelID INT
DECLARE @PerkLevel INT = 1

INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         @PerkLevel , -- Level - int
         1 , -- Price - int
         'Deals 1d8 damage to a single target.'
    )

SET @PerkLevelID = SCOPE_IDENTITY()
SET @PerkLevel = @PerkLevel + 1

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         0   -- RequiredRank - int
    )




INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         @PerkLevel , -- Level - int
         1 , -- Price - int
         'Deals 2d6 damage to a single target.'
    )

SET @PerkLevelID = SCOPE_IDENTITY()
SET @PerkLevel = @PerkLevel + 1

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         6   -- RequiredRank - int
    )



INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         @PerkLevel , -- Level - int
         2 , -- Price - int
         'Deals 4d4 damage to a single target.'
    )

SET @PerkLevelID = SCOPE_IDENTITY()
SET @PerkLevel = @PerkLevel + 1

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         16   -- RequiredRank - int
    )




INSERT INTO dbo.PerkLevels ( PerkID ,
                             Level ,
                             Price ,
                             Description )
VALUES ( @PerkID , -- PerkID - int
         @PerkLevel , -- Level - int
         3 , -- Price - int
         'Deals 5d4 damage to a single target.'
    )

SET @PerkLevelID = SCOPE_IDENTITY()
SET @PerkLevel = @PerkLevel + 1

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
         @PerkLevel , -- Level - int
         4 , -- Price - int
         'Deals 4d8 damage to a single target.'
    )

SET @PerkLevelID = SCOPE_IDENTITY()
SET @PerkLevel = @PerkLevel + 1

INSERT INTO dbo.PerkLevelSkillRequirements ( PerkLevelID ,
                                             SkillID ,
                                             RequiredRank )
VALUES ( @PerkLevelID , -- PerkLevelID - int
         @SkillID , -- SkillID - int
         40   -- RequiredRank - int
    )







-- rollback
-- commit

