
SELECT tfp.TerritoryFlagPermissionID ,
       tfp.Name ,
       tfp.IsActive ,
       tfp.IsSelectable
FROM dbo.TerritoryFlagPermissions tfp
WHERE tfp.TerritoryFlagPermissionID = :permissionID
