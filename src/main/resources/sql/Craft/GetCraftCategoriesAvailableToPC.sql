SELECT cbc.CraftBlueprintCategoryID ,
       cbc.Name ,
       cbc.IsActive
FROM dbo.CraftBlueprintCategories cbc
WHERE CraftBlueprintCategoryID IN (

	SELECT cb.CraftCategoryID
	FROM dbo.PCBlueprints pcb
	JOIN dbo.CraftBlueprints cb ON cb.CraftBlueprintID = pcb.CraftBlueprintID
	WHERE pcb.PlayerID = :playerID
)