SELECT
	COUNT(ap.AppointmentId)
FROM Appointment ap
INNER JOIN Property p on ap.PropertyId = p.PropertyId
INNER JOIN Users buyer on ap.BuyerId = buyer.UserId
INNER JOIN Users seller on ap.BuyerId = seller.UserId
LEFT JOIN Users agent on ap.BuyerId = agent.UserId