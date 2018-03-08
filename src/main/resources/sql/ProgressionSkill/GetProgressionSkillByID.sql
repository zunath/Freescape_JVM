
SELECT ps.SkillID ,
       ps.Name ,
       ps.Description ,
       ps.MaxUpgrades ,
       ps.SoftCap ,
       ps.InitialPrice ,
       ps.IsFeat ,
       ps.IsDisabled
FROM dbo.ProgressionSkills ps
WHERE ps.SkillID = :skillID