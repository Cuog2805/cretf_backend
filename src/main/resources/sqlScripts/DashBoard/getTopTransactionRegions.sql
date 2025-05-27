WITH RegionTransactions AS (
    SELECT
        l.Name as regionName,
        l.Code as regionCode,
        COUNT(dc.DepositContractId) as transactionCount,
        AVG(d.Value) as avgDepositValue,
        d.ScaleUnit as scaleUnit,
        SUM(d.Value) as totalDepositValue
    FROM DepositContract dc
    INNER JOIN Property p ON dc.PropertyId = p.PropertyId
    INNER JOIN Location l ON p.LocationId = l.LocationId
    INNER JOIN Deposit d ON dc.DepositId = d.DepositId
    INNER JOIN Status s ON dc.StatusId = s.StatusId
    WHERE p.Type = :propertyType
      --AND s.Code = 'CONFIRMED'
      AND p.IsDeleted = 0
      AND dc.DateCreated >= DATEADD(month, -6, GETDATE())
    GROUP BY l.LocationId, l.Code, l.Name, d.ScaleUnit
    HAVING COUNT(dc.DepositContractId) > 0
)
SELECT
    NEWID() as dashBoardId,
    regionName as title,
    CAST(transactionCount AS FLOAT) as value,
    scaleUnit as scaleUnit,
    CAST(ROW_NUMBER() OVER (ORDER BY transactionCount DESC) AS INT) as sortOrder,
    CAST(avgDepositValue AS FLOAT) as avgValue,
    CAST(totalDepositValue AS FLOAT) as totalValue
FROM RegionTransactions
ORDER BY sortOrder
OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY