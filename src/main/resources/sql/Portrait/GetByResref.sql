
SELECT TOP 1
	p.PortraitID ,
	p.Resref ,
	p.[2DAID]
FROM dbo.Portraits p
WHERE p.Resref = :resref