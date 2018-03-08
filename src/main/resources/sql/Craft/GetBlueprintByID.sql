

SELECT cb.CraftBlueprintID ,
       cb.CraftID ,
       cb.CraftCategoryID ,
       cb.Level ,
       cb.ItemName ,
       cb.ItemResref ,
       cb.Quantity
FROM dbo.CraftBlueprints cb
WHERE cb.CraftBlueprintID = :craftBlueprintID