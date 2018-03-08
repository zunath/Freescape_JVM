
DECLARE @flagID INT = :flagID
SELECT
(
	SELECT COUNT(1) AS Count
	FROM dbo.ConstructionSites cs
	WHERE cs.PCTerritoryFlagID = @flagID
) + (
	SELECT COUNT(1) AS Count
	FROM dbo.PCTerritoryFlagsStructures pctfs
	WHERE pctfs.PCTerritoryFlagID = @flagID
)

