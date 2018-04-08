
SELECT i.Resref ,
       i.AC ,
       i.ItemTypeID ,
       i.RecommendedLevel ,
       i.LoggingBonus ,
       i.MiningBonus ,
       i.CastingSpeed ,
       i.CraftBonusMetalworking ,
       i.CraftBonusWeaponsmith ,
       i.CraftBonusArmorsmith ,
       i.CraftBonusCooking ,
       i.DurabilityPoints,
       i.AssociatedSkillID,
       i.CraftTierLevel,
       i.CraftBonusWoodworking,
       i.Weight
FROM dbo.Items i
WHERE i.Resref = :resref