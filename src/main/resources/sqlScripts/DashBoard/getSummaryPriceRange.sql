WITH LatestPrices AS (
    SELECT
        PropertyId,
        Value,
        ScaleUnit,
        DateCreated,
        ROW_NUMBER() OVER (PARTITION BY PropertyId ORDER BY DateCreated DESC) as rn
    FROM PropertyPriceHistory
    WHERE IsDeleted = 0
),
PropertiesSoldWithPriceRange AS (
    SELECT
        p.PropertyId as PropertyId,
        p.Type as PropertyType,
--        CASE
--            WHEN lp.Value < 1 THEN N'1_BILL_LESS'
--            WHEN lp.Value >= 1 AND lp.Value < 2 THEN N'1_BILL_TO_2_BILL'
--            WHEN lp.Value >= 2 AND lp.Value < 3 THEN N'2_BILL_TO_3_BILL'
--            WHEN lp.Value >= 3 AND lp.Value < 5 THEN N'3_BILL_TO_5_BILL'
--            WHEN lp.Value >= 5 AND lp.Value < 10 THEN N'5_BILL_TO_10_BILL'
--            WHEN lp.Value >= 10 AND lp.Value < 20 THEN N'10_BILL_TO_20_BILL'
--            WHEN lp.Value >= 20 AND lp.Value < 30 THEN N'20_BILL_TO_30_BILL'
--            WHEN lp.Value >= 30 AND lp.Value < 50 THEN N'30_BILL_TO_50_BILL'
--            ELSE N'50_BILL_OVER'
--        END as code,
        CASE
            WHEN lp.Value < 1 THEN N'Dưới 1 tỷ'
            WHEN lp.Value >= 1 AND lp.Value < 2 THEN N'1-2 tỷ'
            WHEN lp.Value >= 2 AND lp.Value < 3 THEN N'2-3 tỷ'
            WHEN lp.Value >= 3 AND lp.Value < 5 THEN N'3-5 tỷ'
            WHEN lp.Value >= 5 AND lp.Value < 10 THEN N'5-10 tỷ'
            WHEN lp.Value >= 10 AND lp.Value < 20 THEN N'10-20 tỷ'
            WHEN lp.Value >= 20 AND lp.Value < 30 THEN N'20-30 tỷ'
            WHEN lp.Value >= 30 AND lp.Value < 50 THEN N'30-50 tỷ'
            ELSE N'Trên 50 tỷ'
        END as title,
        CASE
            WHEN lp.Value < 1 THEN 1
            WHEN lp.Value >= 1 AND lp.Value < 2 THEN 2
            WHEN lp.Value >= 2 AND lp.Value < 3 THEN 3
            WHEN lp.Value >= 3 AND lp.Value < 5 THEN 4
            WHEN lp.Value >= 5 AND lp.Value < 10 THEN 5
            WHEN lp.Value >= 10 AND lp.Value < 20 THEN 6
            WHEN lp.Value >= 20 AND lp.Value < 30 THEN 7
            WHEN lp.Value >= 30 AND lp.Value < 50 THEN 8
            ELSE 9
        END as sortOrder
    FROM Property p
    INNER JOIN PropertyType pt ON p.PropertyTypeId = pt.PropertyTypeId
    INNER JOIN LatestPrices lp ON p.PropertyId = lp.PropertyId AND lp.rn = 1
    WHERE p.IsDeleted = 0 AND p.Type = 'SOLD'
),
PropertiesRentWithPriceRange AS (
    SELECT
        p.PropertyId as PropertyId,
        p.Type as PropertyType,
--        CASE
--            WHEN lp.Value < 1 THEN N'1_MILL_LESS'
--            WHEN lp.Value >= 1 AND lp.Value < 2 THEN N'1_MILL_TO_2_MILL'
--            WHEN lp.Value >= 2 AND lp.Value < 3 THEN N'2_MILL_TO_3_MILL'
--            WHEN lp.Value >= 3 AND lp.Value < 5 THEN N'3_MILL_TO_5_MILL'
--            WHEN lp.Value >= 5 AND lp.Value < 10 THEN N'5_MILL_TO_10_MILL'
--            WHEN lp.Value >= 10 AND lp.Value < 20 THEN N'10_MILL_TO_20_MILL'
--            WHEN lp.Value >= 20 AND lp.Value < 30 THEN N'20_MILL_TO_30_MILL'
--            WHEN lp.Value >= 30 AND lp.Value < 50 THEN N'30_MILL_TO_50_MILL'
--            ELSE N'50_MILL_OVER'
--        END as code,
        CASE
            WHEN lp.Value < 1 THEN N'Dưới 1 triệu'
            WHEN lp.Value >= 1 AND lp.Value < 2 THEN N'1-2 triệu'
            WHEN lp.Value >= 2 AND lp.Value < 3 THEN N'2-3 triệu'
            WHEN lp.Value >= 3 AND lp.Value < 5 THEN N'3-5 triệu'
            WHEN lp.Value >= 5 AND lp.Value < 10 THEN N'5-10 triệu'
            WHEN lp.Value >= 10 AND lp.Value < 20 THEN N'10-20 triệu'
            WHEN lp.Value >= 20 AND lp.Value < 30 THEN N'20-30 triệu'
            WHEN lp.Value >= 30 AND lp.Value < 50 THEN N'30-50 triệu'
            ELSE N'Trên 50 triệu'
        END as title,
        CASE
            WHEN lp.Value < 1 THEN 1
            WHEN lp.Value >= 1 AND lp.Value < 2 THEN 2
            WHEN lp.Value >= 2 AND lp.Value < 3 THEN 3
            WHEN lp.Value >= 3 AND lp.Value < 5 THEN 4
            WHEN lp.Value >= 5 AND lp.Value < 10 THEN 5
            WHEN lp.Value >= 10 AND lp.Value < 20 THEN 6
            WHEN lp.Value >= 20 AND lp.Value < 30 THEN 7
            WHEN lp.Value >= 30 AND lp.Value < 50 THEN 8
            ELSE 9
        END as sortOrder
    FROM Property p
    INNER JOIN PropertyType pt ON p.PropertyTypeId = pt.PropertyTypeId
    INNER JOIN LatestPrices lp ON p.PropertyId = lp.PropertyId AND lp.rn = 1
    WHERE p.IsDeleted = 0 AND p.Type = 'RENT'
),
PropertiesWithPriceRange AS (
    SELECT PropertyId, PropertyType, title, sortOrder FROM PropertiesSoldWithPriceRange
    UNION ALL
    SELECT PropertyId, PropertyType, title, sortOrder FROM PropertiesRentWithPriceRange
)
SELECT
    NEWID() as dashBoardId,
    title,
    CAST(COUNT(*) AS FLOAT) as value,
    CASE
        WHEN SUM(COUNT(*)) OVER(PARTITION BY PropertyType) = 0 THEN 0.0
        ELSE CAST(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(PARTITION BY PropertyType) AS FLOAT)
    END as percentage,
    sortOrder
FROM PropertiesWithPriceRange
WHERE PropertyType = :propertyType
GROUP BY PropertyType, title, sortOrder
ORDER BY sortOrder