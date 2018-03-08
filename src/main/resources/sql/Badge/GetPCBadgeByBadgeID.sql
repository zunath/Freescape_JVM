
SELECT pcb.PCBadgeID ,
       pcb.PlayerID ,
       pcb.BadgeID ,
       pcb.AcquiredDate ,
       pcb.AcquiredAreaName ,
       pcb.AcquiredAreaTag ,
       pcb.AcquiredAreaResref
FROM dbo.PCBadges pcb
WHERE pcb.PlayerID = :playerID
	AND pcb.BadgeID = :badgeID