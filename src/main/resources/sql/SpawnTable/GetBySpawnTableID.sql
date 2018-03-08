
SELECT st.SpawnTableID ,
       st.Name
FROM dbo.SpawnTables st
WHERE st.SpawnTableID = :spawnTableID