SELECT ad.AuthorizedDMID ,
       ad.Name ,
       ad.CDKey ,
       ad.DMRole ,
       ad.IsActive
FROM dbo.AuthorizedDMs ad
WHERE ad.CDKey = :cdKey
	AND ad.IsActive = 1