SELECT
	p.PropertyId	        as propertyId,
	p.Code			        as code,
	p.Name			        as name,
	p.AddressSpecific       as addressSpecific,
	p.StatusId 				as statusIds,
	pt.Code			        as propertyTypeCode,
	pt.Name			        as propertyTypeName
FROM Property p
INNER JOIN PropertyType pt on p.PropertyTypeId = pt.PropertyTypeId
WHERE p.IsDeleted = 0