SELECT CustomEffectID ,
       Name ,
       IconID ,
       ScriptHandler ,
       StartMessage ,
       ContinueMessage ,
       WornOffMessage
FROM dbo.CustomEffects
WHERE CustomEffectID = :customEffectID