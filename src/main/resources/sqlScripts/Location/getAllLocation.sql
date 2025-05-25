SELECT
    l.LocationId    					AS locationId,
    l.ParentCode    					AS parentCode,
    l.Code          					AS code,
    l.Path          					AS path,
    l.Level         					AS level,
    l.Name          					AS name,
    CASE
    	WHEN l.ParentCode is null THEN l.Name
    	WHEN l.ParentCode is not null THEN l.Name + ', ' + ldad.Name
    	ELSE ''
    END AS fullname,
    l.Description   					AS description,
    l.DateCreated   					AS dateCreated,
    l.Creator       					AS creator,
    l.DateModified  					AS dateModified,
    l.Modifier      					AS modifier
FROM Location l
LEFT JOIN Location ldad on l.ParentCode = ldad.Code
WHERE l.IsDeleted = 0
ORDER BY l.ParentCode, l.Name