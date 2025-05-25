SELECT
    ranked.ApprovalId as approvalId,
    ranked.PropertyId as propertyId,
	ranked.Approver as approver,
	ranked.ApprovalDate as approvalDate
FROM (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY PropertyId ORDER BY ApprovalDate DESC) AS rank
    FROM ApprovalHistory ah
    WHERE PropertyId IN (:propertyIds)
) AS ranked
WHERE rank = 1;