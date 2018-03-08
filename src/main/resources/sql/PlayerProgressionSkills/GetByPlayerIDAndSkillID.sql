
SELECT pps.PlayerProgressionSkillID ,
       pps.PlayerID ,
       pps.ProgressionSkillID ,
       pps.UpgradeLevel ,
       pps.IsSoftCapUnlocked
FROM dbo.PlayerProgressionSkills pps
WHERE pps.PlayerID = :playerID
	AND pps.ProgressionSkillID = :progressionSkillID