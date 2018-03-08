

SELECT TOP 1
	sc.ServerConfigurationID ,
    sc.ServerName ,
    sc.MessageOfTheDay
FROM dbo.ServerConfiguration sc
ORDER BY sc.ServerConfigurationID DESC