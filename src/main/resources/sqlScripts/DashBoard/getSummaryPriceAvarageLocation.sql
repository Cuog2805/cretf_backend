WITH LatestPrices AS (
    SELECT
        PropertyId,
        Value,
        ScaleUnit,
        ROW_NUMBER() OVER (PARTITION BY PropertyId ORDER BY DateCreated DESC) as rn
    FROM PropertyPriceHistory
    WHERE IsDeleted = 0
),
RootLocations AS (
    SELECT LocationId, Code, Name
    FROM Location
    WHERE Level = 1 AND IsDeleted = 0
),
ChildLocations AS (
    SELECT
        rl.LocationId as RootLocationId,
        rl.Code as RootCode,
        rl.Name as RootName,
        cl.LocationId as ChildLocationId,
        cl.Code as ChildCode,
        cl.Name as ChildName
    FROM RootLocations rl
    INNER JOIN Location cl
        ON (cl.Path LIKE '%' + rl.LocationId + '%' OR cl.ParentCode = rl.Code)
        AND cl.IsDeleted = 0
)
SELECT
    NEWID() as dashBoardId,
    cl.RootName as title,
    COALESCE(CAST(AVG(lp.Value) AS FLOAT), 0.0) as value,
    CAST(ROW_NUMBER() OVER (ORDER BY AVG(lp.Value) DESC) AS INT) as sortOrder,
    CAST(COUNT(*) AS FLOAT) as count,
    pt.Name as category
FROM Property p
INNER JOIN ChildLocations cl ON p.LocationId = cl.ChildLocationId
INNER JOIN PropertyType pt ON p.PropertyTypeId = pt.PropertyTypeId
INNER JOIN LatestPrices lp ON p.PropertyId = lp.PropertyId AND lp.rn = 1
WHERE p.IsDeleted = 0 AND p.Type = :propertyType
GROUP BY cl.RootLocationId, cl.RootCode, cl.RootName, pt.Name
ORDER BY sortOrder