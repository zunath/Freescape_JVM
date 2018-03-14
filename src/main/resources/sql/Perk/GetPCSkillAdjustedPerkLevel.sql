SELECT PerkLevelID ,
       PerkID ,
       Level ,
       Price ,
       Description
FROM dbo.fn_GetPlayerEffectivePerkLevel(:playerID, :perkID, NULl)