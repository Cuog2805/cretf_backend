WITH MonthlyData AS (
    SELECT
        FORMAT(dc.DateCreated, 'MM-yyyy') as depositContractDate,
        s.Code as type,
        s.Name as name,
        COUNT(*) as depositContractCount,
        ROW_NUMBER() OVER (
            PARTITION BY s.Code
            ORDER BY YEAR(dc.DateCreated) DESC, MONTH(dc.DateCreated) DESC
        ) as rn
    FROM DepositContract dc
    INNER JOIN Property p ON dc.PropertyId = p.PropertyId
    INNER JOIN Status s ON dc.StatusId = s.StatusId and s.Type = 'DEPOSIT_STATUS'
    WHERE p.Type = :propertyType
    GROUP BY FORMAT(dc.DateCreated, 'MM-yyyy'), YEAR(dc.DateCreated), MONTH(dc.DateCreated), s.Code, s.Name
)
SELECT
    NEWID() as dashBoardId,
    depositContractDate as depositContractDate,
    type as type,
    name as name,
    depositContractCount as depositContractCount
FROM MonthlyData
WHERE rn <= 12
ORDER BY depositContractDate DESC, type