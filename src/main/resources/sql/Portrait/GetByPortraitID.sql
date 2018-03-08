
SELECT
	p.PortraitID ,
	p.Resref ,
	p.[2DAID]
FROM dbo.Portraits p
WHERE p.PortraitID = :portraitID