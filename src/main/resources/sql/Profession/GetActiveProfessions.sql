
SELECT pd.ProfessionID ,
       pd.Name ,
       pd.Description ,
       pd.Bonuses ,
       pd.IsActive
FROM dbo.ProfessionsDomain pd
WHERE pd.IsActive = 1 