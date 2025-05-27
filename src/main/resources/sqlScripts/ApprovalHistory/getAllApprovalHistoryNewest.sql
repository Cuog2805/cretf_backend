SELECT
    ranked.ApprovalId as approvalId,
    ranked.EntityTableId as entityTableId,
	ranked.Approver as approver,
	ranked.ApprovalDate as approvalDate,
	ranked.TableName as tableName
FROM (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY EntityTableId ORDER BY ApprovalDate DESC) AS rank
    FROM ApprovalHistory ah
    WHERE EntityTableId IN (:entityTableIds)
) AS ranked
WHERE rank = 1