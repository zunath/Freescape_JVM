
SELECT lt.LootTableID ,
       lt.Name
FROM dbo.LootTables lt
WHERE lt.LootTableID = :lootTableID