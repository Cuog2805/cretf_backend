SELECT
    ranked.PropertyPriceHistoryId as propertyPriceHistoryId,
    ranked.PropertyId as propertyId,
	ranked.Value as value,
	sc.Unit as scaleUnit,
	ranked.DateCreated as dateCreated,
	ranked.Creator as creator,
	ranked.IsDeleted as isDeleted
FROM (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY PropertyId ORDER BY DateCreated DESC) AS rank
    FROM PropertyPriceHistory
    WHERE PropertyId IN (:propertyIds)
    AND IsDeleted = 0
) AS ranked
INNER JOIN Scale sc on ranked.ScaleUnit = sc.ScaleId
WHERE rank = 1;