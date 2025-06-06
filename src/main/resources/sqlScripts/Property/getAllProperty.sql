WITH LatestPrices AS (
    SELECT
        PropertyId,
        Value,
        ScaleUnit,
        ROW_NUMBER() OVER (PARTITION BY PropertyId ORDER BY DateCreated DESC) as rn
    FROM PropertyPriceHistory
    WHERE IsDeleted = 0
)
SELECT
    p.PropertyId            					as propertyId,
    p.Code                  					as code,
    p.Name                  					as name,
    p.AddressSpecific       					as addressSpecific,
    p.StatusId              					as statusIds,
    pt.PropertyTypeId       					as propertyTypeId,
    pt.Code                 					as propertyTypeCode,
    pt.Name                 					as propertyTypeName,
    CAST(lp.Value as nvarchar)+ ' ' + s.Unit 	as priceNewest,
    lp.Value                					as priceNewestValue,
    lp.ScaleUnit 								as priceNewestScale,
    p.LocationId            					as locationId,
    p.Creator               					as creator,
    p.DateCreated           					as dateCreated,
    p.Type										as type,
    p.IsDeleted                                 as isDeleted,
    p.Views										as views,
    uf.Username 								as usernameFav,
    CASE
        WHEN uf.Username IS NOT NULL THEN 1
        ELSE 0
    END 										as isInFavourite
FROM Property p
INNER JOIN PropertyType pt ON p.PropertyTypeId = pt.PropertyTypeId
INNER JOIN LatestPrices lp ON p.PropertyId = lp.PropertyId AND lp.rn = 1
INNER JOIN Scale s ON lp.ScaleUnit = s.ScaleId
LEFT JOIN UserFavourite uf ON p.PropertyId = uf.PropertyId AND uf.Username = :usernameFav
WHERE (p.IsDeleted = :isDeleted or :isDeleted is null)
AND (EXISTS (
    SELECT 1
    FROM OPENJSON(p.StatusId) p_status
    WHERE p_status.value IN (
        SELECT value
        FROM OPENJSON(ISNULL(:statusIds, '[]'))
    )
) OR :statusIds IS NULL)