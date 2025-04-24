SELECT
	s.StatusId 						as statusId,
	s.Code 							as code,
	s.Name 							as name,
	s.Type							as type,
	s.Color							as color,
	s.Creator 						as creator,
	s.DateCreated  					as dateCreated,
	s.Modifier 						as modifier,
	s.DateModified  				as dateModified
FROM Status s