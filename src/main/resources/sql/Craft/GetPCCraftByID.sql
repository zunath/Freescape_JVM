
SELECT pcc.PCCraftID ,
       pcc.PlayerID ,
       pcc.CraftID ,
       pcc.Level ,
       pcc.Experience
FROM dbo.PCCrafts pcc
WHERE pcc.PlayerID = :playerID
	AND pcc.CraftID = :craftID