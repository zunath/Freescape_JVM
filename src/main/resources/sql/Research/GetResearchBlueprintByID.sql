

SELECT rb.ResearchBlueprintID ,
       rb.CraftBlueprintID ,
       rb.IsActive ,
       rb.Price ,
       rb.ResearchPoints ,
       rb.SkillRequired
FROM dbo.ResearchBlueprints rb
WHERE rb.ResearchBlueprintID = :researchBlueprintID