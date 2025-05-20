SELECT
	ap.AppointmentId 	as appointmentId,
	ap.PropertyId 		as propertyId,
	p.AddressSpecific   as propertyAddress,
	ap.BuyerId 			as buyerId,
	buyer.Username 		as buyer,
	ap.SellerId 		as sellerId,
	seller.Username 	as seller,
	ap.AgentId 			as agentId,
	agent.Username 		as agent,
	ap.Type 			as type,
	ap.Date 			as date,
	ap.StatusId 		as statusId,
	ap.Note    			as note,
	ap.Creator 			as creator,
	ap.DateCreated 		as dateCreated,
	ap.Modifier 		as modifier,
	ap.DateModified 	as dateModified
FROM Appointment ap
INNER JOIN Property p on ap.PropertyId = p.PropertyId
INNER JOIN Users buyer on ap.BuyerId = buyer.Username
INNER JOIN Users seller on ap.SellerId  = seller.Username
LEFT JOIN Users agent on ap.AgentId = agent.Username