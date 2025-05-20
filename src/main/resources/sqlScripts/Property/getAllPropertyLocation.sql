SELECT
    p.PropertyId            					as propertyId,
    p.LocationId            					as locationId
FROM Property p
WHERE (p.LocationId = :locationId or :locationId is null)