
SELECT rb.ResearchBlueprintID ,
       rb.CraftBlueprintID ,
       rb.IsActive ,
       rb.Price ,
       rb.ResearchPoints ,
       rb.SkillRequired
FROM dbo.ResearchBlueprints rb
JOIN dbo.CraftBlueprints cb ON cb.CraftBlueprintID = rb.CraftBlueprintID
JOIN dbo.CraftBlueprintCategories cbc ON cbc.CraftBlueprintCategoryID = cb.CraftCategoryID
WHERE rb.SkillRequired <= :skillRequired
	 AND cbc.CraftBlueprintCategoryID = :craftBlueprintCategoryID
	 AND cb.CraftID = :craftID
ORDER BY cb.ItemName ASC
