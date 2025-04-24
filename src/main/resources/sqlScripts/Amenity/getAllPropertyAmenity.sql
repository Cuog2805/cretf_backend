SELECT
	pa.PropertyAmenityId 						as propertyAmenityId,
	pa.PropertyId 						        as propertyId,
	a.AmenityId 								as amenityId,
	a.Code 										as code,
	a.Name 										as name,
	a.Description 								as description,
	a.AmenityType 								as amenityType,
	a.IsGeneralInfo 							as isGeneralInfo,
	cs.Name 									as amenityTypeName,
	pa.Value									as value,
	sc.Unit 									as scaleUnit,
	CASE
	WHEN cs.Name = N'Phòng' THEN CAST(pa.Value AS nvarchar) + ' ' + a.Name
	WHEN cs.Name = N'Tiện ích' THEN a.Name
	WHEN cs.Name = N'Thông tin khác' THEN CAST(pa.Value AS nvarchar) + ' ' + sc.Unit
	ELSE ''
	END as valueDisplay,
	pa.Level 									as level
FROM PropertyAmenity pa
INNER JOIN Amenity a on pa.AmenityId = a.AmenityId
INNER JOIN CategoryShared cs on cs.CategoryType = 'AMENITY_TYPE' and cs.Code = a.AmenityType
INNER JOIN Scale sc on pa.ScaleUnit = sc.ScaleId