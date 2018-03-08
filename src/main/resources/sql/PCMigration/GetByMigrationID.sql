
SELECT pcm.PCMigrationID ,
       pcm.Name
FROM dbo.PCMigrations pcm
WHERE pcm.PCMigrationID = :pcMigrationID
