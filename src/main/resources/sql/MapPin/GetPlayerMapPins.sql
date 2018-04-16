
SELECT pcmp.PCMapPinID ,
       pcmp.PlayerID ,
       pcmp.AreaTag ,
       pcmp.PositionX ,
       pcmp.PositionY ,
       pcmp.NoteText
FROM dbo.PCMapPins pcmp
WHERE pcmp.PlayerID = :playerID