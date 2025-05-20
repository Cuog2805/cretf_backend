SELECT
	pnp.PublicFacilityId AS publicFacilityId,
	--pnp.PropertyId AS propertyId,
    --pf.PublicFacilityId AS publicFacilityId,
	pnp.Distance AS distance,
    pf.LocationId AS locationId,
    pf.Name AS name
FROM PropertyNearbyPlace pnp
INNER JOIN PublicFacility pf on pnp.PublicFacilityId = pf.PublicFacilityId
INNER JOIN Property p on pnp.PropertyId = p.PropertyId
WHERE (pnp.PropertyId = :propertyId or :propertyId is null)