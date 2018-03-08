
SELECT pcss.PCSearchSiteID ,
       pcss.PlayerID ,
       pcss.SearchSiteID ,
       pcss.UnlockDateTime
FROM dbo.PCSearchSites pcss
WHERE pcss.PlayerID = :playerID
	AND pcss.SearchSiteID = :searchSiteID