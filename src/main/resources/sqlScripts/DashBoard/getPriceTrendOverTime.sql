WITH MonthlyPriceTrend AS (
    SELECT
        FORMAT(pph.DateCreated, 'MM-yyyy') as monthYear,
        FORMAT(pph.DateCreated, 'yyyy-MM') as sortKey,
        AVG(pph.Value) as avgPrice,
        COUNT(*) as propertyCount,
        MIN(pph.Value) as minPrice,
        MAX(pph.Value) as maxPrice
    FROM PropertyPriceHistory pph
    INNER JOIN Property p ON pph.PropertyId = p.PropertyId
    WHERE pph.IsDeleted = 0
      AND p.IsDeleted = 0
      AND p.Type = :propertyType
      AND pph.DateCreated >= DATEADD(month, -12, GETDATE())
    GROUP BY FORMAT(pph.DateCreated, 'MM-yyyy'), FORMAT(pph.DateCreated, 'yyyy-MM'),
             YEAR(pph.DateCreated), MONTH(pph.DateCreated)
)
SELECT
    NEWID() as dashBoardId,
    monthYear as title,
    ROUND(CAST(avgPrice AS FLOAT), 2) AS value,
    CAST(ROW_NUMBER() OVER (ORDER BY sortKey) AS INT) as sortOrder,
    CAST(propertyCount AS FLOAT) as count,
    CAST(minPrice AS FLOAT) as minValue,
    CAST(maxPrice AS FLOAT) as maxValue
FROM MonthlyPriceTrend
ORDER BY sortOrder