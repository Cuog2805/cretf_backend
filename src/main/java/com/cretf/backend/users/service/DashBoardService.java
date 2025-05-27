package com.cretf.backend.users.service;

import com.cretf.backend.users.dto.DashBoardDTO;

import java.util.List;

public interface DashBoardService {
    public List<DashBoardDTO> getSummaryPriceRange(DashBoardDTO dashBoardDTO) throws Exception;
    public List<DashBoardDTO> getSummaryTransaction(DashBoardDTO dashBoardDTO) throws Exception;
    public List<DashBoardDTO> getSummaryPriceAvarageLocation(DashBoardDTO dashBoardDTO) throws Exception;
    public List<DashBoardDTO> getSummaryTotalStat(DashBoardDTO dashBoardDTO) throws Exception;
    public List<DashBoardDTO> getTopTransactionRegions(DashBoardDTO dashBoardDTO) throws Exception;
    public List<DashBoardDTO> getPriceTrendOverTime(DashBoardDTO dashBoardDTO) throws Exception;
    public List<DashBoardDTO> getPropertyTypeStatic(DashBoardDTO dashBoardDTO) throws Exception;
}
