
SELECT tfp.TerritoryFlagPermissionID ,
       tfp.Name ,
       tfp.IsActive ,
       tfp.IsSelectable
FROM dbo.TerritoryFlagPermissions tfp
WHERE tfp.IsActive = 1
	AND tfp.IsSelectable = 1