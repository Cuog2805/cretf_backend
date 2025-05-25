SELECT
	a.AmenityId         AS amenityId,
	a.Code              AS code,
	a.Name              AS name,
	a.Description       AS description,
	a.AmenityType       AS amenityType,
	sc.ScaleId 			as scaleId,
	sc.Unit 			as scaleUnit,
	a.Creator           AS creator,
	a.DateCreated       AS dateCreated,
	a.Modifier          AS modifier,
	a.DateModified      AS dateModified,
	a.Ordinal 			AS ordinal
FROM Amenity a
INNER JOIN Scale sc on a.ScaleUnit = sc.ScaleId
WHERE a.IsDeleted = 0
ORDER BY a.AmenityType, a.Ordinal