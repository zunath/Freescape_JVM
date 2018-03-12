

SELECT p.PerkID ,
       p.Name ,
       p.Price,
       p.FeatID ,
       p.IsActive ,
       p.JavaScriptName ,
       p.BaseManaCost ,
       p.BaseCastingTime ,
       p.Description ,
       p.AbilityCategoryID ,
       p.AbilityCooldownCategoryID ,
       p.IsQueuedWeaponSkill
FROM dbo.Perks pf
WHERE a.PerkID = :featID