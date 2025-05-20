WITH
PropertyViewsStats AS (
    SELECT SUM(p.Views) AS totalViews
    FROM Property p
    WHERE p.IsDeleted = 0 AND p.Type = :propertyType
),
LatestPrices AS (
    SELECT
        PropertyId,
        Value,
        ScaleUnit,
        DateCreated,
        ROW_NUMBER() OVER (PARTITION BY PropertyId ORDER BY DateCreated DESC) as rn
    FROM PropertyPriceHistory
    WHERE IsDeleted = 0
),
CompletedContractsStats AS (
    SELECT
        COUNT(*) AS depositContractCount,
        COALESCE(CAST(SUM(lp.Value) AS FLOAT), 0) AS totalDepositContractValue
    FROM DepositContract dc
    INNER JOIN Status s on dc.StatusId = s.StatusId
    INNER JOIN Deposit d on dc.DepositId = d.DepositId
    INNER JOIN Property p on dc.PropertyId = p.PropertyId
    INNER JOIN LatestPrices lp on p.PropertyId = lp.PropertyId
    WHERE s.Code = 'CONFIRMED' AND p.Type = :propertyType
),
PropertiesActiveStats AS (
    SELECT COUNT(*) AS totalPropertyActive
    FROM Property p
    INNER JOIN Status s on s.StatusId = s.StatusId
    WHERE s.Code = 'FORSOLD' and p.Type = :propertyType
)
SELECT
    pvs.totalViews as totalViews,
    ccs.depositContractCount as depositContractCount,
    ccs.totalDepositContractValue as totalDepositContractValue,
    pas.totalPropertyActive as totalPropertyActive
FROM PropertyViewsStats pvs
CROSS JOIN CompletedContractsStats ccs
CROSS JOIN PropertiesActiveStats pas