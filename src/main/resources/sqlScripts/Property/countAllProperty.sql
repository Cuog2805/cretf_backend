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
    COUNT(p.PropertyId)
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