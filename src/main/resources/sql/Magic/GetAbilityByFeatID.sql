

SELECT a.AbilityID ,
       a.Name ,
       a.FeatID ,
       a.IsActive ,
       a.JavaScriptName ,
       a.BaseManaCost ,
       a.BaseCastingTime ,
       a.Description ,
       a.AbilityCategoryID ,
       a.AbilityCooldownCategoryID ,
       a.IsQueuedWeaponSkill
FROM dbo.Abilities a
WHERE a.FeatID = :featID