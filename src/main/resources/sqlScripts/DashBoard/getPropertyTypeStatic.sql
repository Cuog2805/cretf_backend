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
    NEWID() as dashBoardId,
    pt.Name as title,
    CAST(COUNT(*) AS FLOAT) as value,
    CAST(ROW_NUMBER() OVER (ORDER BY COUNT(*) DESC) AS INT) as sortOrder,
    COALESCE(CAST(AVG(lp.Value) AS FLOAT), 0.0) as avgValue,
    CAST(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER() AS FLOAT) as percentage
FROM Property p
INNER JOIN PropertyType pt ON p.PropertyTypeId = pt.PropertyTypeId
INNER JOIN LatestPrices lp ON p.PropertyId = lp.PropertyId AND lp.rn = 1
WHERE p.IsDeleted = 0 AND p.Type = :propertyType
GROUP BY pt.PropertyTypeId, pt.Code, pt.Name
ORDER BY sortOrder