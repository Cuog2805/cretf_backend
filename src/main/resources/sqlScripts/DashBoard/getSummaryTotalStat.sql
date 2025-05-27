WITH PropertyViewsStats AS (
    SELECT SUM(COALESCE(p.Views, 0)) AS totalViews
    FROM Property p
    WHERE p.IsDeleted = 0 AND p.Type = :propertyType
),
CompletedContractsStats AS (
    SELECT
        COUNT(*) AS depositContractCount,
        COALESCE(CAST(SUM(d.Value) AS FLOAT), 0) AS totalDepositContractValue
    FROM DepositContract dc
    INNER JOIN Deposit d ON dc.DepositId = d.DepositId
    INNER JOIN Property p ON dc.PropertyId = p.PropertyId
    WHERE p.Type = :propertyType
),
PropertiesActiveStats AS (
    SELECT COUNT(*) AS totalPropertyActive
    FROM Property p
    WHERE p.Type = :propertyType
      AND p.IsDeleted = 0
      AND EXISTS (
          SELECT 1
          FROM OPENJSON(p.StatusId) json_status
          INNER JOIN Status s ON s.StatusId = json_status.value
          WHERE s.Code = 'FORSOLD'
      )
),
TotalPropertiesStats AS (
    SELECT COUNT(*) AS totalProperties
    FROM Property p
    WHERE p.IsDeleted = 0 AND p.Type = :propertyType
)
SELECT
    NEWID() as dashBoardId,
    N'Tổng số bất động sản' as title,
    CAST(COALESCE(tps.totalProperties, 0) AS float) as value,
    1 as sortOrder
FROM TotalPropertiesStats tps
UNION ALL
--SELECT
--    N'Tổng lượt xem' as title, COALESCE(pvs.totalViews, 0) as value, 2 as sortOrder
--FROM PropertyViewsStats pvs
--UNION ALL
SELECT
    NEWID() as dashBoardId,
    N'Số lượng giao dịch' as title,
    CAST(COALESCE(ccs.depositContractCount, 0) AS float) as value,
    3 as sortOrder
FROM CompletedContractsStats ccs
--UNION ALL
--SELECT
--    NEWID() as dashBoardId,
--    N'Khối lượng đang giao dịch' as title,
--    COALESCE(ccs.totalDepositContractValue, 0) as value,
--    4 as sortOrder
--FROM CompletedContractsStats ccs
--UNION ALL
--SELECT
--	NEWID() as dashBoardId,
--    N'Số lượng đang chào bán' as title,
--    CAST(COALESCE(pas.totalPropertyActive, 0) AS float) as value,
--    5 as sortOrder
--FROM PropertiesActiveStats pas
ORDER BY sortOrder