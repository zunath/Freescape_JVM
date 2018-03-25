
SELECT cb.CraftBlueprintID ,
       cb.CraftCategoryID ,
       cb.Level ,
       cb.ItemName ,
       cb.ItemResref ,
       cb.Quantity ,
       cb.SkillID ,
       cb.CraftDeviceID ,
       cb.PerkID ,
       cb.RequiredPerkLevel ,
       cb.IsActive ,
       cb.CraftTierLevel
FROM dbo.CraftBlueprints cb
WHERE cb.CraftBlueprintID = :blueprintID