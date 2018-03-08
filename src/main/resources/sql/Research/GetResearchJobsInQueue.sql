
SELECT pcq.PCStructureResearchID ,
       pcq.PCStructureID ,
       pcq.ResearchBlueprintID ,
       pcq.ResearchSlot ,
       pcq.StartDateTime ,
       pcq.CompletedDateTime ,
       pcq.IsCanceled ,
       pcq.DeliverDateTime
FROM dbo.PCTerritoryFlagsStructuresResearchQueues pcq
WHERE pcq.PCStructureID = :pcStructureID
	AND pcq.IsCanceled = 0
	AND pcq.DeliverDateTime IS NULL