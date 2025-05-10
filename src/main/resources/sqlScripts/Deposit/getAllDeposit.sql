SELECT
    d.DepositId     AS depositId,
    d.PropertyId    AS propertyId,
    d.Value         AS value,
    d.SacleUnit     AS scaleUnit,
    d.DueDate       AS dueDate
FROM Deposit d