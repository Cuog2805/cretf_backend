SELECT
	cs.CategorySharedId		as categorySharedId,
	cs.Code					as code,
	cs.CodeParent			as codeParent,
	cs.Level				as level,
	cs.Name					as name,
	cs.Path                 as path,
	cs.Component            as component,
	cs.Icon                 as icon,
	cs.Access               as access,
	cs.CategoryType         as categoryType
FROM CategoryShared as cs
WHERE cs.IsDeleted = 0