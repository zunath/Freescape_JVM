
SELECT pcb.PCBlueprintID ,
       pcb.PlayerID ,
       pcb.CraftBlueprintID ,
       pcb.AcquiredDate
FROM dbo.PCBlueprints pcb
JOIN dbo.CraftBlueprints cb ON cb.CraftBlueprintID = pcb.CraftBlueprintID
WHERE pcb.PlayerID = :playerID
	AND cb.CraftCategoryID = :categoryID
	AND cb.CraftID = :craftID