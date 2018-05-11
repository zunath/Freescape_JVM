SELECT ID,
	SUM(t.VanityCount) AS VanityCount,
	SUM(t.SpecialCount) AS SpecialCount,
	SUM(t.ResourceCount) AS ResourceCount,
	SUM(t.BuildingCount) AS BuildingCount
FROM
(
	SELECT 1 AS ID,
		SUM(CASE WHEN sb.IsVanity=1 THEN 1 ELSE 0 END) AS VanityCount,
		SUM(CASE WHEN sb.IsSpecial=1 THEN 1 ELSE 0 END) AS SpecialCount,
		SUM(CASE WHEN sb.IsResource=1 THEN 1 ELSE 0 END) AS ResourceCount,
		SUM(CASE WHEN sb.IsBuilding=1 THEN 1 ELSE 0 END) AS BuildingCount
	FROM dbo.ConstructionSites cs
	JOIN dbo.StructureBlueprints sb ON sb.StructureBlueprintID = cs.StructureBlueprintID
	WHERE cs.PCTerritoryFlagID = :flagID
		AND cs.IsActive = 1
	UNION
	SELECT 1 AS ID,
		SUM(CASE WHEN sb.IsVanity=1 THEN 1 ELSE 0 END) AS VanityCount,
		SUM(CASE WHEN sb.IsSpecial=1 THEN 1 ELSE 0 END) AS SpecialCount,
		SUM(CASE WHEN sb.IsResource=1 THEN 1 ELSE 0 END) AS ResourceCount,
		SUM(CASE WHEN sb.IsBuilding=1 THEN 1 ELSE 0 END) AS BuildingCount
	FROM dbo.PCTerritoryFlagsStructures pctfs
	JOIN dbo.StructureBlueprints sb ON sb.StructureBlueprintID = pctfs.StructureBlueprintID
	WHERE pctfs.PCTerritoryFlagID = :flagID
		AND pctfs.IsActive = 1
) t
GROUP BY t.ID
