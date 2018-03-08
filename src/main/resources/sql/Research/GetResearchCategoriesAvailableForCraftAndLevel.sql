

SELECT DISTINCT
	   cbc.CraftBlueprintCategoryID ,
       cbc.Name ,
       cbc.IsActive
FROM dbo.ResearchBlueprints rb
JOIN dbo.CraftBlueprints cb ON cb.CraftBlueprintID = rb.CraftBlueprintID
JOIN dbo.CraftBlueprintCategories cbc ON cbc.CraftBlueprintCategoryID = cb.CraftCategoryID
WHERE cb.CraftID = :craftID
	AND rb.SkillRequired <= :skillRequired


