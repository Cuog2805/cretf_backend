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
LEFT JOIN UserFavourite uf ON p.PropertyId = uf.PropertyId
WHERE (uf.Username = :username)