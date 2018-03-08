

SELECT pcb.PCBlueprintID ,
       pcb.PlayerID ,
       pcb.CraftBlueprintID ,
       pcb.AcquiredDate
FROM dbo.PCBlueprints pcb
WHERE pcb.CraftBlueprintID = :craftBlueprintID
	AND pcb.PlayerID = :playerID