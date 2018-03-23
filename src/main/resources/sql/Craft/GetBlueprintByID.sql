
SELECT c.CraftBlueprintID ,
       c.CraftCategoryID ,
       c.Level ,
       c.ItemName ,
       c.ItemResref ,
       c.Quantity ,
       c.SkillID ,
       c.CraftDeviceID ,
       c.PerkID
FROM dbo.CraftBlueprints c
WHERE c.CraftBlueprintID = :blueprintID