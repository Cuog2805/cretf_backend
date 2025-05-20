SELECT
    COUNT(pf.PublicFacilityId)
FROM
    PublicFacility pf
WHERE IsDeleted = 0