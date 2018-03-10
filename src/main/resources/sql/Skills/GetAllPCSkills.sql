
SELECT PCSkillID ,
       PlayerID ,
       SkillID ,
       XP ,
       Rank,
       IsLocked
FROM dbo.PCSkills
WHERE PlayerID = :playerID