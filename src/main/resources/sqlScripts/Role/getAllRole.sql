SELECT
	r.RoleId 						as roleId,
	r.Name 							as name,
	r.Description 					as description,
	r.Creator 						as creator,
	r.DateCreated  					as dateCreated,
	r.Modifier 						as modifier,
	r.DateModified  				as dateModified,
	r.IsDeleted						as isDeleted
FROM Role r