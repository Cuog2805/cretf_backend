SELECT
    pf.PublicFacilityId AS publicFacilityId,
    pf.LocationId AS locationId,
    pf.Name AS name,
    pf.DateCreated AS dateCreated,
    pf.Creator AS creator,
    pf.DateModified AS dateModified,
    pf.Modifier AS modifier,
    pf.IsDeleted AS isDeleted
FROM
    PublicFacility pf