
SELECT zc.ZombieClothesID ,
       zc.Resref ,
       zc.IsActive
FROM dbo.ZombieClothes zc
WHERE zc.Resref != ''
	AND zc.IsActive = 1