WITH MonthlyData AS (
    SELECT
        FORMAT(dc.DateCreated, 'MM-yyyy') as monthYear,
        FORMAT(dc.DateCreated, 'yyyy-MM') as sortKey,
        COUNT(*) as transactionCount,
        ROW_NUMBER() OVER (
            PARTITION BY s.Code
            ORDER BY YEAR(dc.DateCreated) DESC, MONTH(dc.DateCreated) DESC
        ) as rn
    FROM DepositContract dc
    INNER JOIN Property p ON dc.PropertyId = p.PropertyId
    INNER JOIN Status s ON dc.StatusId = s.StatusId AND s.Type = 'DEPOSIT_STATUS'
    WHERE p.Type = :propertyType
    GROUP BY FORMAT(dc.DateCreated, 'MM-yyyy'), FORMAT(dc.DateCreated, 'yyyy-MM'),
             YEAR(dc.DateCreated), MONTH(dc.DateCreated), s.Code, s.Name
)
SELECT
    NEWID() as dashBoardId,
    monthYear as title,
    CAST(transactionCount AS FLOAT) as value,
    CAST(ROW_NUMBER() OVER (ORDER BY sortKey DESC) as INT) as sortOrder
FROM MonthlyData
WHERE rn <= 12
ORDER BY sortOrder DESC