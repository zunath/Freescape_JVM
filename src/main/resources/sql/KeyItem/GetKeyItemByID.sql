
SELECT ki.KeyItemID ,
       ki.KeyItemCategoryID ,
       ki.Name ,
       ki.Description
FROM dbo.KeyItems ki
WHERE ki.KeyItemID = :keyItemID