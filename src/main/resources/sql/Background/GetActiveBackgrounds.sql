
SELECT b.BackgroundID ,
       b.Name ,
       b.Description ,
       b.Bonuses ,
       b.IsActive
FROM dbo.Backgrounds b
WHERE b.IsActive = 1