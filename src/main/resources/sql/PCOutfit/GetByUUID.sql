
SELECT pco.PlayerID ,
       pco.Outfit1 ,
       pco.Outfit2 ,
       pco.Outfit3 ,
       pco.Outfit4 ,
       pco.Outfit5 ,
       pco.Outfit6 ,
       pco.Outfit7 ,
       pco.Outfit8 ,
       pco.Outfit9 ,
       pco.Outfit10
FROM dbo.PCOutfits pco
WHERE pco.PlayerID = :playerID