SELECT
    dc.DepositContractId AS depositContractId,
    dc.FileId AS fileId,
    dc.FileName AS fileName,
    dc.DepositId AS depositId,
    dc.PropertyId AS propertyId,
    dc.Seller AS seller,
    dc.Buyer AS buyer,
    dc.DateCreated AS dateCreated,
    dc.DownloadUrl AS downloadUrl,
    dc.StatusId AS statusId
FROM DepositContract dc