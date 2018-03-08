
SELECT
	p.PortraitID ,
	p.Resref ,
	p.[2DAID]
FROM dbo.Portraits p
WHERE [p].[2DAID] = :_2DAID