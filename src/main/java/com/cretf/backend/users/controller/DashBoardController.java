package com.cretf.backend.users.controller;

import com.cretf.backend.product.entity.Property;
import com.cretf.backend.users.dto.DashBoardDTO;
import com.cretf.backend.users.dto.DepositContractDTO;
import com.cretf.backend.users.service.DashBoardService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final DashBoardService dashBoardService;

    public DashBoardController(DashBoardService dashBoardService) {
        this.dashBoardService = dashBoardService;
    }

    @PostMapping("/getSummaryPriceRange")
    public Response<List<DashBoardDTO>> getSummaryPriceRange(@RequestBody DashBoardDTO request) throws Exception {
        log.debug("Rest request to getSummaryPriceRange {}", request);
        List<DashBoardDTO> result = dashBoardService.getSummaryPriceRange(request);
        return Response.ok(result);
    }

    @PostMapping("/getSummaryTransaction")
    public Response<List<DashBoardDTO>> getSummaryTransaction(@RequestBody DashBoardDTO request) throws Exception {
        log.debug("Rest request to getSummaryTransaction");
        List<DashBoardDTO> result = dashBoardService.getSummaryTransaction(request);
        return Response.ok(result);
    }

    @PostMapping("/getPropertyTypeStatic")
    public Response<List<DashBoardDTO>> getPropertyTypeStatic(@RequestBody DashBoardDTO request) throws Exception {
        log.debug("Rest request to getPropertyTypeStatic");
        List<DashBoardDTO> result = dashBoardService.getPropertyTypeStatic(request);
        return Response.ok(result);
    }

    @PostMapping("/getPriceTrendOverTime")
    public Response<List<DashBoardDTO>> getPriceTrendOverTime(@RequestBody DashBoardDTO request) throws Exception {
        log.debug("Rest request to getPriceTrendOverTime");
        List<DashBoardDTO> result = dashBoardService.getPriceTrendOverTime(request);
        return Response.ok(result);
    }

    @PostMapping("/getTopTransactionRegions")
    public Response<List<DashBoardDTO>> getTopTransactionRegions(@RequestBody DashBoardDTO request) throws Exception {
        log.debug("Rest request to getTopTransactionRegions");
        List<DashBoardDTO> result = dashBoardService.getTopTransactionRegions(request);
        return Response.ok(result);
    }

    @PostMapping("/getSummaryPriceAvarageLocation")
    public Response<List<DashBoardDTO>> getSummaryPriceAvarageLocation(@RequestBody DashBoardDTO request) throws Exception {
        log.debug("Rest request to getSummaryPriceAvarageLocation");
        List<DashBoardDTO> result = dashBoardService.getSummaryPriceAvarageLocation(request);
        return Response.ok(result);
    }

    @PostMapping("/getSummaryTotalStat")
    public Response<List<DashBoardDTO>> getSummaryTotalStat(@RequestBody DashBoardDTO request) throws Exception {
        log.debug("Rest request to getSummaryTotalStat");
        List<DashBoardDTO> result = dashBoardService.getSummaryTotalStat(request);
        return Response.ok(result);
    }
}
