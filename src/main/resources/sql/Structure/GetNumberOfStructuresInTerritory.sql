DECLARE @flagID INT = :flagID
DECLARE @IsVanity BIT = :isVanity
DECLARE @IsSpecial BIT = :isSpecial
SELECT
(
	SELECT COUNT(1) AS Count
	FROM dbo.ConstructionSites cs
	JOIN dbo.StructureBlueprints sb ON sb.StructureBlueprintID = cs.StructureBlueprintID
	WHERE cs.PCTerritoryFlagID = @flagID
		AND sb.IsVanity = @IsVanity
		AND sb.IsSpecial = @IsSpecial
) + (
	SELECT COUNT(1) AS Count
	FROM dbo.PCTerritoryFlagsStructures pctfs
	JOIN dbo.StructureBlueprints sb ON sb.StructureBlueprintID = pctfs.StructureBlueprintID
	WHERE pctfs.PCTerritoryFlagID = @flagID
		AND sb.IsVanity = @IsVanity
		AND sb.IsSpecial = @IsSpecial
)
