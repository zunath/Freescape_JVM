
SELECT b.BadgeID ,
       b.Name ,
       b.Description ,
       b.Experience
FROM dbo.Badges b
WHERE b.BadgeID = :badgeID