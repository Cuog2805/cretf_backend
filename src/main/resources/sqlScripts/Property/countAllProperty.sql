SELECT
	COUNT(p.PropertyId)
FROM Property p
INNER JOIN PropertyType pt on p.PropertyTypeId = pt.PropertyTypeId
WHERE p.IsDeleted = 0