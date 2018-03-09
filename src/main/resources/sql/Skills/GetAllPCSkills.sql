
SELECT PCSkillID ,
       PlayerID ,
       SkillID ,
       XP ,
       Rank
FROM dbo.PCSkills
WHERE PlayerID = :playerID