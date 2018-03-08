

SELECT sc.StorageContainerID ,
       sc.AreaName ,
       sc.AreaTag ,
       sc.AreaResref
FROM dbo.StorageContainers sc
WHERE sc.StorageContainerID = :containerID