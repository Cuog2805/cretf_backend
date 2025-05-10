SELECT
	s.ScaleId  						as scaleId,
	s.Code 							as code,
	s.Unit 							as unit,
	s.Description					as description,
	s.Type							as type,
	s.Creator 						as creator,
	s.DateCreated  					as dateCreated,
	s.Modifier 						as modifier,
	s.DateModified  				as dateModified
FROM Scale s
WHERE s.IsDeleted = 0