SELECT
	COUNT(s.StatusId)
FROM Status s
WHERE s.IsDeleted = 0