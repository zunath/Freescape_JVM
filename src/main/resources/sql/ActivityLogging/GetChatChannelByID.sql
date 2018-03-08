SELECT ccd.ChatChannelID ,
       ccd.Name
FROM dbo.ChatChannelsDomain ccd
WHERE ccd.ChatChannelID = :chatChannelID